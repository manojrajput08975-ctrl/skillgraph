package com.skillgraph.repository;

import com.skillgraph.dto.SkillDto;
import com.skillgraph.dto.TechnologyDto;
import org.neo4j.driver.Driver;
import org.neo4j.driver.Record;
import org.neo4j.driver.Session;
import org.neo4j.driver.Value;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;
import java.util.Optional;

@Repository
public class TechnologyRepository {

    private final Driver driver;

    public TechnologyRepository(Driver driver) { this.driver = driver; }

    public List<TechnologyDto> findAll() {
        try (Session s = driver.session()) {
            return s.run("MATCH (t:Technology) RETURN t ORDER BY t.name")
                    .list(r -> toDto(r.get("t")));
        }
    }

    public Optional<TechnologyDto> findById(String id) {
        try (Session s = driver.session()) {
            String q = """
                MATCH (t:Technology {id: $id})
                OPTIONAL MATCH (t)-[:TECH_REQUIRES]->(sk:Skill)
                RETURN t, collect(DISTINCT {id: sk.id, name: sk.name, category: sk.category}) AS skills
                """;
            List<Record> rows = s.run(q, Map.of("id", id)).list();
            if (rows.isEmpty()) return Optional.empty();
            Record rec = rows.get(0);
            if (rec.get("t").isNull()) return Optional.empty();
            TechnologyDto dto = toDto(rec.get("t"));
            dto.requiredSkills = rec.get("skills").asList(v -> {
                SkillDto sk = new SkillDto();
                sk.id       = v.get("id").asString(null);
                sk.name     = v.get("name").asString(null);
                sk.category = v.get("category").asString(null);
                return sk;
            }).stream().filter(sk -> sk.id != null).toList();
            return Optional.of(dto);
        }
    }

    private TechnologyDto toDto(Value v) {
        TechnologyDto dto = new TechnologyDto();
        dto.id   = v.get("id").asString(null);
        dto.name = v.get("name").asString(null);
        dto.type = v.get("type").asString(null);
        return dto;
    }
}
