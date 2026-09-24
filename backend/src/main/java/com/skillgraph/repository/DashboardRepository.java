package com.skillgraph.repository;

import com.skillgraph.dto.DashboardDto;
import org.neo4j.driver.Driver;
import org.neo4j.driver.Session;
import org.springframework.stereotype.Repository;

@Repository
public class DashboardRepository {

    private final Driver driver;

    public DashboardRepository(Driver driver) { this.driver = driver; }

    public DashboardDto getCounts() {
        try (Session s = driver.session()) {
            DashboardDto dto = new DashboardDto();
            dto.totalCandidates    = s.run("MATCH (n:User)       RETURN count(n) AS c").single().get("c").asLong();
            dto.totalSkills        = s.run("MATCH (n:Skill)      RETURN count(n) AS c").single().get("c").asLong();
            dto.totalProjects      = s.run("MATCH (n:Project)    RETURN count(n) AS c").single().get("c").asLong();
            dto.totalTechnologies  = s.run("MATCH (n:Technology) RETURN count(n) AS c").single().get("c").asLong();
            dto.totalRoles         = s.run("MATCH (n:JobRole)    RETURN count(n) AS c").single().get("c").asLong();
            dto.totalCompanies     = s.run("MATCH (n:Company)    RETURN count(n) AS c").single().get("c").asLong();
            dto.totalResources     = s.run("MATCH (n:Resource)   RETURN count(n) AS c").single().get("c").asLong();
            dto.totalRelationships = s.run("MATCH ()-[r]->()     RETURN count(r) AS c").single().get("c").asLong();
            return dto;
        }
    }
}
