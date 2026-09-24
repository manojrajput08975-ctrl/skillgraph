package com.skillgraph.repository;

import com.skillgraph.dto.CompanyDto;
import com.skillgraph.dto.RecommendationDto;
import org.neo4j.driver.Driver;
import org.neo4j.driver.Session;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;

@Repository
public class RecommendationRepository {

    private final Driver driver;

    public RecommendationRepository(Driver driver) { this.driver = driver; }

    /**
     * For a given user, rank all job roles by how many of their required skills
     * the user already has. Also returns missing skills and hiring companies.
     * This is a 3-hop graph traversal: User→Skill→JobRole←Skill, JobRole→Company
     */
    public List<RecommendationDto> recommendRoles(String userId) {
        try (Session s = driver.session()) {
            String q = """
                MATCH (u:User {id: $userId})-[:USER_HAS_SKILL]->(userSkill:Skill)
                MATCH (r:JobRole)<-[:SKILL_REQUIRED_FOR]-(required:Skill)
                WITH r,
                     count(DISTINCT required) AS totalRequired,
                     count(DISTINCT CASE WHEN (u)-[:USER_HAS_SKILL]->(required) THEN required END) AS matchedSkills
                WHERE totalRequired > 0
                WITH r, totalRequired, matchedSkills,
                     round(100.0 * matchedSkills / totalRequired) AS matchPct
                ORDER BY matchPct DESC, matchedSkills DESC
                OPTIONAL MATCH (r)-[:ROLE_AT]->(c:Company)
                RETURN r.id AS roleId, r.title AS roleTitle, r.level AS level,
                       matchedSkills, totalRequired, matchPct,
                       collect(DISTINCT {id: c.id, name: c.name, industry: c.industry}) AS companies
                LIMIT 10
                """;
            return s.run(q, Map.of("userId", userId)).list(rec -> {
                RecommendationDto dto = new RecommendationDto();
                dto.roleId        = rec.get("roleId").asString();
                dto.roleTitle     = rec.get("roleTitle").asString();
                dto.level         = rec.get("level").asString();
                dto.matchedSkills = rec.get("matchedSkills").asLong();
                dto.totalRequired = rec.get("totalRequired").asLong();
                dto.matchPct      = rec.get("matchPct").asDouble();
                dto.companies     = rec.get("companies").asList(v -> {
                    CompanyDto c = new CompanyDto();
                    c.id       = v.get("id").asString(null);
                    c.name     = v.get("name").asString(null);
                    c.industry = v.get("industry").asString(null);
                    return c;
                }).stream().filter(c -> c.id != null).toList();
                return dto;
            });
        }
    }

    /**
     * Skill-gap: skills required for a role that the user does NOT yet have,
     * plus learning resources for each missing skill.
     */
    public List<RecommendationDto> skillGap(String userId, String roleId) {
        try (Session s = driver.session()) {
            String q = """
                MATCH (r:JobRole {id: $roleId})<-[:SKILL_REQUIRED_FOR]-(required:Skill)
                WHERE NOT EXISTS {
                    MATCH (u:User {id: $userId})-[:USER_HAS_SKILL]->(required)
                }
                OPTIONAL MATCH (res:Resource)-[:RESOURCE_TEACHES]->(required)
                RETURN required.id AS skillId, required.name AS skillName,
                       required.category AS category,
                       collect(DISTINCT res.title) AS resources
                """;
            return s.run(q, Map.of("userId", userId, "roleId", roleId)).list(rec -> {
                RecommendationDto dto = new RecommendationDto();
                dto.roleId        = roleId;
                dto.missingSkills = List.of(rec.get("skillName").asString());
                return dto;
            });
        }
    }
}
