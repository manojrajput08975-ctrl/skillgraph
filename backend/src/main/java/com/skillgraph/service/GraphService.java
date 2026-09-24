package com.skillgraph.service;

import com.skillgraph.dto.GraphDto;
import com.skillgraph.exception.DatabaseException;
import com.skillgraph.repository.GraphRepository;
import org.neo4j.driver.exceptions.Neo4jException;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Service
public class GraphService {

    private final GraphRepository repo;

    public GraphService(GraphRepository repo) { this.repo = repo; }

    public GraphDto neighbourhood(String nodeId) {
        if (nodeId == null || nodeId.isBlank()) {
            throw new IllegalArgumentException("nodeId must not be blank");
        }
        try { return repo.neighbourhood(nodeId); }
        catch (Neo4jException ex) { throw new DatabaseException("Failed to load graph neighbourhood", ex); }
    }

    public GraphDto careerPathGraph() {
        try { return repo.careerPathGraph(); }
        catch (Neo4jException ex) { throw new DatabaseException("Failed to load career path graph", ex); }
    }

    public List<Map<String, String>> multiHopTraversal() {
        try { return repo.multiHopTraversal(); }
        catch (Neo4jException ex) { throw new DatabaseException("Failed to run multi-hop traversal", ex); }
    }
}
