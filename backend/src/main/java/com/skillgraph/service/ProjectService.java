package com.skillgraph.service;

import com.skillgraph.dto.ProjectDto;
import com.skillgraph.exception.DatabaseException;
import com.skillgraph.exception.ResourceNotFoundException;
import com.skillgraph.repository.ProjectRepository;
import org.neo4j.driver.exceptions.Neo4jException;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ProjectService {

    private final ProjectRepository repo;

    public ProjectService(ProjectRepository repo) { this.repo = repo; }

    public List<ProjectDto> getAll() {
        try { return repo.findAll(); }
        catch (Neo4jException ex) { throw new DatabaseException("Failed to load projects", ex); }
    }

    public ProjectDto getById(String id) {
        try {
            return repo.findById(id)
                    .orElseThrow(() -> new ResourceNotFoundException("Project", id));
        } catch (Neo4jException ex) {
            throw new DatabaseException("Failed to load project " + id, ex);
        }
    }
}
