package com.skillgraph.service;

import com.skillgraph.dto.JobRoleDto;
import com.skillgraph.exception.DatabaseException;
import com.skillgraph.exception.ResourceNotFoundException;
import com.skillgraph.repository.JobRoleRepository;
import org.neo4j.driver.exceptions.Neo4jException;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class JobRoleService {

    private final JobRoleRepository repo;

    public JobRoleService(JobRoleRepository repo) { this.repo = repo; }

    public List<JobRoleDto> getAll() {
        try { return repo.findAll(); }
        catch (Neo4jException ex) { throw new DatabaseException("Failed to load roles", ex); }
    }

    public JobRoleDto getById(String id) {
        try {
            return repo.findById(id)
                    .orElseThrow(() -> new ResourceNotFoundException("JobRole", id));
        } catch (Neo4jException ex) {
            throw new DatabaseException("Failed to load role " + id, ex);
        }
    }
}
