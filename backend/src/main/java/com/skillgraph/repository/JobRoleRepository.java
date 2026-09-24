package com.skillgraph.repository;

import com.skillgraph.dto.CompanyDto;
import com.skillgraph.dto.JobRoleDto;
import com.skillgraph.dto.SkillDto;
import org.neo4j.driver.Driver;
import org.neo4j.driver.Record;
import org.neo4j.driver.Session;
import org.neo4j.driver.Value;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;
import java.util.Optional;

@Repository
public class JobRoleRepository {

    private final Driver driver;

    public JobRoleRepository(Driver driver) { this.driver = driver; }

    public List<JobRoleDto> findAll() {
        try (Session s = driver.session()) {
            return s.run("MATCH (r:JobRole) RETURN r ORDER BY r.title")
                    .list(rec -> toDto(rec.get("r")));
        }
    }

    public Optional<JobRoleDto> findById(String id) {
        try (Session s = driver.session()) {
            String q = """
                MATCH (r:JobRole {id: $id})
                OPTIONAL MATCH (sk:Skill)-[:SKILL_REQUIRED_FOR]->(r)
                OPTIONAL MATCH (r)-[:ROLE_AT]->(c:Company)
                RETURN r,
                       collect(DISTINCT {id: sk.id, name: sk.name, category: sk.category}) AS skills,
                       collect(DISTINCT {id: c.id, name: c.name, industry: c.industry, size: c.size}) AS companies
                """;
            List<Record> rows = s.run(q, Map.of("id", id)).list();
            if (rows.isEmpty()) return Optional.empty();
            Record rec = rows.get(0);
            if (rec.get("r").isNull()) return Optional.empty();
            JobRoleDto dto = toDto(rec.get("r"));
            dto.requiredSkills = rec.get("skills").asList(v -> {
                SkillDto sk = new SkillDto();
                sk.id       = v.get("id").asString(null);
                sk.name     = v.get("name").asString(null);
                sk.category = v.get("category").asString(null);
                return sk;
            }).stream().filter(sk -> sk.id != null).toList();
            dto.companies = rec.get("companies").asList(v -> {
                CompanyDto c = new CompanyDto();
                c.id       = v.get("id").asString(null);
                c.name     = v.get("name").asString(null);
                c.industry = v.get("industry").asString(null);
                c.size     = v.get("size").asString(null);
                return c;
            }).stream().filter(c -> c.id != null).toList();
            return Optional.of(dto);
        }
    }

    private JobRoleDto toDto(Value v) {
        JobRoleDto dto = new JobRoleDto();
        dto.id    = v.get("id").asString(null);
        dto.title = v.get("title").asString(null);
        dto.level = v.get("level").asString(null);
        return dto;
    }
}
