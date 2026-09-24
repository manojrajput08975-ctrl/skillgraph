package com.skillgraph.repository;

import com.skillgraph.dto.ProjectDto;
import com.skillgraph.dto.TechnologyDto;
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
public class ProjectRepository {

    private final Driver driver;

    public ProjectRepository(Driver driver) { this.driver = driver; }

    public List<ProjectDto> findAll() {
        try (Session s = driver.session()) {
            return s.run("MATCH (p:Project) RETURN p ORDER BY p.name")
                    .list(r -> toDto(r.get("p")));
        }
    }

    public Optional<ProjectDto> findById(String id) {
        try (Session s = driver.session()) {
            String q = """
                MATCH (p:Project {id: $id})
                OPTIONAL MATCH (p)-[:PROJECT_USES]->(t:Technology)
                OPTIONAL MATCH (u:User)-[wo:USER_WORKED_ON]->(p)
                RETURN p,
                       collect(DISTINCT {id: t.id, name: t.name, type: t.type}) AS techs,
                       collect(DISTINCT {id: u.id, name: u.name, role: wo.role}) AS contributors
                """;
            List<Record> rows = s.run(q, Map.of("id", id)).list();
            if (rows.isEmpty()) return Optional.empty();
            Record rec = rows.get(0);
            if (rec.get("p").isNull()) return Optional.empty();
            ProjectDto dto = toDto(rec.get("p"));
            dto.technologies = rec.get("techs").asList(v -> {
                TechnologyDto t = new TechnologyDto();
                t.id   = v.get("id").asString(null);
                t.name = v.get("name").asString(null);
                t.type = v.get("type").asString(null);
                return t;
            }).stream().filter(t -> t.id != null).toList();
            dto.contributors = rec.get("contributors").asList(v -> {
                UserDto u = new UserDto();
                u.id            = v.get("id").asString(null);
                u.name          = v.get("name").asString(null);
                u.contributorRole = v.get("role").asString(null);
                return u;
            }).stream().filter(u -> u.id != null).toList();
            return Optional.of(dto);
        }
    }

    private ProjectDto toDto(Value v) {
        ProjectDto dto = new ProjectDto();
        dto.id     = v.get("id").asString(null);
        dto.name   = v.get("name").asString(null);
        dto.status = v.get("status").asString(null);
        dto.domain = v.get("domain").asString(null);
        return dto;
    }
}
