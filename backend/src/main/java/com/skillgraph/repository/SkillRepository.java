package com.skillgraph.repository;

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
public class SkillRepository {

    private final Driver driver;

    public SkillRepository(Driver driver) { this.driver = driver; }

    public List<SkillDto> findAll() {
        try (Session s = driver.session()) {
            return s.run("MATCH (sk:Skill) RETURN sk ORDER BY sk.name")
                    .list(r -> toDto(r.get("sk")));
        }
    }

    public Optional<SkillDto> findById(String id) {
        try (Session s = driver.session()) {
            String q = """
                MATCH (sk:Skill {id: $id})
                OPTIONAL MATCH (sk)-[:SKILL_RELATED_TO]->(rel:Skill)
                OPTIONAL MATCH (sk)-[:SKILL_REQUIRED_FOR]->(r:JobRole)
                RETURN sk,
                       collect(DISTINCT {id: rel.id, name: rel.name, category: rel.category}) AS related,
                       collect(DISTINCT {id: r.id, title: r.title, level: r.level}) AS roles
                """;
            List<Record> rows = s.run(q, Map.of("id", id)).list();
            if (rows.isEmpty()) return Optional.empty();
            Record rec = rows.get(0);
            if (rec.get("sk").isNull()) return Optional.empty();
            SkillDto dto = toDto(rec.get("sk"));
            dto.relatedSkills = rec.get("related").asList(v -> {
                SkillDto r = new SkillDto();
                r.id = v.get("id").asString(null);
                r.name = v.get("name").asString(null);
                r.category = v.get("category").asString(null);
                return r;
            }).stream().filter(r -> r.id != null).toList();
            dto.roles = rec.get("roles").asList(v -> {
                JobRoleDto r = new JobRoleDto();
                r.id = v.get("id").asString(null);
                r.title = v.get("title").asString(null);
                r.level = v.get("level").asString(null);
                return r;
            }).stream().filter(r -> r.id != null).toList();
            return Optional.of(dto);
        }
    }

    private SkillDto toDto(Value v) {
        SkillDto dto  = new SkillDto();
        dto.id        = v.get("id").asString(null);
        dto.name      = v.get("name").asString(null);
        dto.category  = v.get("category").asString(null);
        return dto;
    }
}
