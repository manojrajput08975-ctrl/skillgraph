package com.skillgraph.service;

import com.skillgraph.dto.UserDto;
import com.skillgraph.exception.DatabaseException;
import com.skillgraph.exception.ResourceNotFoundException;
import com.skillgraph.repository.UserRepository;
import org.neo4j.driver.exceptions.Neo4jException;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserService {

    private final UserRepository repo;

    public UserService(UserRepository repo) { this.repo = repo; }

    public List<UserDto> getAll() {
        try { return repo.findAll(); }
        catch (Neo4jException ex) { throw new DatabaseException("Failed to load candidates", ex); }
    }

    public UserDto getById(String id) {
        try {
            return repo.findById(id)
                    .orElseThrow(() -> new ResourceNotFoundException("Candidate", id));
        } catch (Neo4jException ex) {
            throw new DatabaseException("Failed to load candidate " + id, ex);
        }
    }
}
