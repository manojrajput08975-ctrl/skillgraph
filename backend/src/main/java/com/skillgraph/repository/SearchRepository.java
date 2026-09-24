package com.skillgraph.repository;

import com.skillgraph.dto.SearchResultDto;
import org.neo4j.driver.Driver;
import org.neo4j.driver.Session;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Repository
public class SearchRepository {

    private final Driver driver;

    public SearchRepository(Driver driver) { this.driver = driver; }

    public List<SearchResultDto> search(String term) {
        String lower = term.toLowerCase();
        List<SearchResultDto> results = new ArrayList<>();

        try (Session s = driver.session()) {
            // Users
            s.run("""
                MATCH (u:User)
                WHERE toLower(u.name) CONTAINS $term OR toLower(u.email) CONTAINS $term
                RETURN u.id AS id, u.name AS name, u.location AS subtitle
                LIMIT 10
                """, Map.of("term", lower))
             .list().forEach(r -> results.add(make(r.get("id").asString(), r.get("name").asString(), "User", r.get("subtitle").asString(null))));

            // Skills
            s.run("""
                MATCH (sk:Skill)
                WHERE toLower(sk.name) CONTAINS $term OR toLower(sk.category) CONTAINS $term
                RETURN sk.id AS id, sk.name AS name, sk.category AS subtitle
                LIMIT 10
                """, Map.of("term", lower))
             .list().forEach(r -> results.add(make(r.get("id").asString(), r.get("name").asString(), "Skill", r.get("subtitle").asString(null))));

            // Projects
            s.run("""
                MATCH (p:Project)
                WHERE toLower(p.name) CONTAINS $term OR toLower(p.domain) CONTAINS $term
                RETURN p.id AS id, p.name AS name, p.domain AS subtitle
                LIMIT 10
                """, Map.of("term", lower))
             .list().forEach(r -> results.add(make(r.get("id").asString(), r.get("name").asString(), "Project", r.get("subtitle").asString(null))));

            // Technologies
            s.run("""
                MATCH (t:Technology)
                WHERE toLower(t.name) CONTAINS $term OR toLower(t.type) CONTAINS $term
                RETURN t.id AS id, t.name AS name, t.type AS subtitle
                LIMIT 10
                """, Map.of("term", lower))
             .list().forEach(r -> results.add(make(r.get("id").asString(), r.get("name").asString(), "Technology", r.get("subtitle").asString(null))));

            // JobRoles
            s.run("""
                MATCH (r:JobRole)
                WHERE toLower(r.title) CONTAINS $term OR toLower(r.level) CONTAINS $term
                RETURN r.id AS id, r.title AS name, r.level AS subtitle
                LIMIT 10
                """, Map.of("term", lower))
             .list().forEach(r -> results.add(make(r.get("id").asString(), r.get("name").asString(), "JobRole", r.get("subtitle").asString(null))));

            // Companies
            s.run("""
                MATCH (c:Company)
                WHERE toLower(c.name) CONTAINS $term OR toLower(c.industry) CONTAINS $term
                RETURN c.id AS id, c.name AS name, c.industry AS subtitle
                LIMIT 10
                """, Map.of("term", lower))
             .list().forEach(r -> results.add(make(r.get("id").asString(), r.get("name").asString(), "Company", r.get("subtitle").asString(null))));
        }
        return results;
    }

    private SearchResultDto make(String id, String name, String type, String subtitle) {
        SearchResultDto dto = new SearchResultDto();
        dto.id       = id;
        dto.name     = name;
        dto.type     = type;
        dto.subtitle = subtitle;
        return dto;
    }
}
