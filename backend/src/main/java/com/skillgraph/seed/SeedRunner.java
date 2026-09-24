package com.skillgraph.seed;

import org.neo4j.driver.Driver;
import org.neo4j.driver.Session;
import org.neo4j.driver.Record;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Profile;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Component;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Activated only with --spring.profiles.active=seed
 * Reads seed/seed.cypher from the classpath, splits on ";" and executes
 * each statement against CognoDB, then runs all 5 verification queries.
 */
@Component
@Profile("seed")
public class SeedRunner implements CommandLineRunner {

    private static final Logger log = LoggerFactory.getLogger(SeedRunner.class);
    private final Driver driver;

    public SeedRunner(Driver driver) {
        this.driver = driver;
    }

    @Override
    public void run(String... args) throws Exception {
        log.info("=== Phase 3 Seed Runner starting ===");
        executeSeedFile();
        verifyCounts();
        runVerificationQueries();
        log.info("=== Seed Runner complete ===");
    }

    // ── Seed execution ────────────────────────────────────────────────────────

    private void executeSeedFile() throws Exception {
        ClassPathResource resource = new ClassPathResource("seed/seed.cypher");
        String content;
        try (BufferedReader reader = new BufferedReader(
                new InputStreamReader(resource.getInputStream(), StandardCharsets.UTF_8))) {
            StringBuilder sb = new StringBuilder();
            String line;
            while ((line = reader.readLine()) != null) {
                String trimmed = line.trim();
                if (!trimmed.startsWith("//") && !trimmed.isEmpty()) {
                    sb.append(line).append("\n");
                }
            }
            content = sb.toString();
        }

        String[] statements = content.split(";");
        int executed = 0;
        int skipped = 0;

        try (Session session = driver.session()) {
            for (String stmt : statements) {
                String s = stmt.trim();
                if (s.isEmpty()) { skipped++; continue; }
                session.run(s).consume();
                executed++;
            }
        }
        log.info("Seed: executed={} skipped={}", executed, skipped);
    }

    // ── Node / relationship counts ────────────────────────────────────────────

    private void verifyCounts() {
        try (Session session = driver.session()) {
            Map<String, String> labelQueries = Map.of(
                "User",       "MATCH (n:User)       RETURN count(n) AS c",
                "Skill",      "MATCH (n:Skill)      RETURN count(n) AS c",
                "Project",    "MATCH (n:Project)    RETURN count(n) AS c",
                "Technology", "MATCH (n:Technology) RETURN count(n) AS c",
                "JobRole",    "MATCH (n:JobRole)    RETURN count(n) AS c",
                "Company",    "MATCH (n:Company)    RETURN count(n) AS c",
                "Resource",   "MATCH (n:Resource)   RETURN count(n) AS c"
            );
            log.info("--- Node counts ---");
            labelQueries.forEach((label, q) -> {
                long count = session.run(q).single().get("c").asLong();
                log.info("  {}: {}", label, count);
            });

            List<String[]> relQueries = List.of(
                new String[]{"USER_HAS_SKILL",            "MATCH ()-[r:USER_HAS_SKILL]->()            RETURN count(r) AS c"},
                new String[]{"USER_WORKED_ON",            "MATCH ()-[r:USER_WORKED_ON]->()            RETURN count(r) AS c"},
                new String[]{"PROJECT_USES",              "MATCH ()-[r:PROJECT_USES]->()              RETURN count(r) AS c"},
                new String[]{"TECH_REQUIRES",             "MATCH ()-[r:TECH_REQUIRES]->()             RETURN count(r) AS c"},
                new String[]{"SKILL_RELATED_TO",          "MATCH ()-[r:SKILL_RELATED_TO]->()          RETURN count(r) AS c"},
                new String[]{"SKILL_REQUIRED_FOR",        "MATCH ()-[r:SKILL_REQUIRED_FOR]->()        RETURN count(r) AS c"},
                new String[]{"ROLE_AT",                   "MATCH ()-[r:ROLE_AT]->()                   RETURN count(r) AS c"},
                new String[]{"RESOURCE_TEACHES",          "MATCH ()-[r:RESOURCE_TEACHES]->()          RETURN count(r) AS c"},
                new String[]{"USER_RECOMMENDED_RESOURCE", "MATCH ()-[r:USER_RECOMMENDED_RESOURCE]->() RETURN count(r) AS c"}
            );
            log.info("--- Relationship counts ---");
            for (String[] pair : relQueries) {
                long count = session.run(pair[1]).single().get("c").asLong();
                log.info("  {}: {}", pair[0], count);
            }
        }
    }

    // ── 5 Verification queries ────────────────────────────────────────────────

    private void runVerificationQueries() {
        try (Session session = driver.session()) {

            // Q1: Multi-hop traversal User→Project→Technology→Skill→JobRole (4 hops)
            log.info("--- Q1: Multi-hop traversal (User→Project→Technology→Skill→JobRole) ---");
            String q1 = """
                MATCH (u:User)-[:USER_WORKED_ON]->(p:Project)
                      -[:PROJECT_USES]->(t:Technology)
                      -[:TECH_REQUIRES]->(s:Skill)
                      -[:SKILL_REQUIRED_FOR]->(r:JobRole)
                RETURN u.name AS user, p.name AS project,
                       t.name AS technology, s.name AS skill, r.title AS role
                LIMIT 10
                """;
            List<Record> q1rows = session.run(q1).list();
            q1rows.forEach(row -> log.info("  {} → {} → {} → {} → {}",
                row.get("user").asString(), row.get("project").asString(),
                row.get("technology").asString(), row.get("skill").asString(),
                row.get("role").asString()));
            log.info("  Q1 rows returned: {}", q1rows.size());

            // Q2: Graph-based job-role recommendation for a given user
            log.info("--- Q2: Job-role recommendation for user-1 ---");
            String q2 = """
                MATCH (u:User {id: $userId})-[:USER_HAS_SKILL]->(s:Skill)
                      -[:SKILL_REQUIRED_FOR]->(r:JobRole)
                WITH r, count(s) AS matchedSkills
                ORDER BY matchedSkills DESC
                RETURN r.title AS role, r.level AS level, matchedSkills
                LIMIT 5
                """;
            List<Record> q2rows = session.run(q2, Map.of("userId", "user-1")).list();
            q2rows.forEach(row -> log.info("  role={} level={} matchedSkills={}",
                row.get("role").asString(), row.get("level").asString(),
                row.get("matchedSkills").asLong()));
            log.info("  Q2 rows returned: {}", q2rows.size());

            // Q3: Skill-gap analysis — skills required for a role that a user lacks
            log.info("--- Q3: Skill-gap analysis (user-6 vs role-3 ML Engineer) ---");
            String q3 = """
                MATCH (r:JobRole {id: $roleId})<-[:SKILL_REQUIRED_FOR]-(required:Skill)
                WHERE NOT EXISTS {
                    MATCH (u:User {id: $userId})-[:USER_HAS_SKILL]->(required)
                }
                RETURN required.name AS missingSkill, required.category AS category
                """;
            List<Record> q3rows = session.run(q3, Map.of("userId", "user-6", "roleId", "role-3")).list();
            q3rows.forEach(row -> log.info("  missing={} category={}",
                row.get("missingSkill").asString(), row.get("category").asString()));
            log.info("  Q3 rows returned: {}", q3rows.size());

            // Q4: Related skills (2-hop: skill → related → related)
            log.info("--- Q4: Related skills to 'Java' (2-hop) ---");
            String q4 = """
                MATCH (s:Skill {id: $skillId})-[:SKILL_RELATED_TO*1..2]->(related:Skill)
                WHERE related.id <> $skillId
                RETURN DISTINCT related.name AS relatedSkill, related.category AS category
                """;
            List<Record> q4rows = session.run(q4, Map.of("skillId", "skill-1")).list();
            q4rows.forEach(row -> log.info("  related={} category={}",
                row.get("relatedSkill").asString(), row.get("category").asString()));
            log.info("  Q4 rows returned: {}", q4rows.size());

            // Q5: Project + technology + contributor traversal
            log.info("--- Q5: Project contributors with their technologies and skills ---");
            String q5 = """
                MATCH (u:User)-[:USER_WORKED_ON]->(p:Project)-[:PROJECT_USES]->(t:Technology)
                MATCH (u)-[:USER_HAS_SKILL]->(s:Skill)<-[:TECH_REQUIRES]-(t)
                RETURN p.name AS project, u.name AS contributor,
                       collect(DISTINCT t.name) AS technologies,
                       collect(DISTINCT s.name) AS matchedSkills
                ORDER BY p.name
                LIMIT 10
                """;
            List<Record> q5rows = session.run(q5).list();
            q5rows.forEach(row -> log.info("  project={} contributor={} techs={} skills={}",
                row.get("project").asString(), row.get("contributor").asString(),
                row.get("technologies").asList(), row.get("matchedSkills").asList()));
            log.info("  Q5 rows returned: {}", q5rows.size());
        }
    }
}
