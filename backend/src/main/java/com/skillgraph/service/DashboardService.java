package com.skillgraph.service;

import com.skillgraph.dto.DashboardDto;
import com.skillgraph.exception.DatabaseException;
import com.skillgraph.repository.DashboardRepository;
import org.neo4j.driver.exceptions.Neo4jException;
import org.springframework.stereotype.Service;

@Service
public class DashboardService {

    private final DashboardRepository repo;

    public DashboardService(DashboardRepository repo) { this.repo = repo; }

    public DashboardDto getCounts() {
        try {
            return repo.getCounts();
        } catch (Neo4jException ex) {
            throw new DatabaseException("Failed to load dashboard counts", ex);
        }
    }
}
