package com.skillgraph.repository;

import com.skillgraph.dto.ProjectDto;
import com.skillgraph.dto.SkillDto;
import com.skillgraph.dto.UserDto;
import org.neo4j.driver.Driver;
import org.neo4j.driver.Record;
import org.neo4j.driver.Session;
import org.neo4j.driver.Value;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;
import java.util.Optional;

@Repository
public class UserRepository {

    private final Driver driver;

    public UserRepository(Driver driver) { this.driver = driver; }

    public List<UserDto> findAll() {
        try (Session s = driver.session()) {
            return s.run("MATCH (u:User) RETURN u ORDER BY u.name")
                    .list(r -> toDto(r.get("u")));
        }
    }

    public Optional<UserDto> findById(String id) {
        try (Session s = driver.session()) {
            String q = """
                MATCH (u:User {id: $id})
                OPTIONAL MATCH (u)-[hs:USER_HAS_SKILL]->(sk:Skill)
                OPTIONAL MATCH (u)-[wo:USER_WORKED_ON]->(p:Project)
                RETURN u,
                       collect(DISTINCT {id: sk.id, name: sk.name, category: sk.category,
                                         level: hs.level, years: hs.years}) AS skills,
                       collect(DISTINCT {id: p.id, name: p.name, status: p.status,
                                         domain: p.domain, role: wo.role}) AS projects
                """;
            List<Record> rows = s.run(q, Map.of("id", id)).list();
            if (rows.isEmpty()) return Optional.empty();
            Record r = rows.get(0);
            UserDto dto = toDto(r.get("u"));
            dto.skills   = r.get("skills").asList(v -> {
                SkillDto sk = new SkillDto();
                sk.id       = v.get("id").asString(null);
                sk.name     = v.get("name").asString(null);
                sk.category = v.get("category").asString(null);
                sk.level    = v.get("level").asString(null);
                sk.years    = v.get("years").isNull() ? null : v.get("years").asInt();
                return sk;
            }).stream().filter(sk -> sk.id != null).toList();
            dto.projects = r.get("projects").asList(v -> {
                ProjectDto p = new ProjectDto();
                p.id     = v.get("id").asString(null);
                p.name   = v.get("name").asString(null);
                p.status = v.get("status").asString(null);
                p.domain = v.get("domain").asString(null);
                p.role   = v.get("role").asString(null);
                return p;
            }).stream().filter(p -> p.id != null).toList();
            return Optional.of(dto);
        }
    }

    private UserDto toDto(Value v) {
        UserDto dto    = new UserDto();
        dto.id         = v.get("id").asString(null);
        dto.name       = v.get("name").asString(null);
        dto.email      = v.get("email").asString(null);
        dto.yearsExp   = v.get("yearsExp").isNull() ? null : v.get("yearsExp").asInt();
        dto.location   = v.get("location").asString(null);
        return dto;
    }
}
