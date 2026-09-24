package com.skillgraph.service;

import com.skillgraph.dto.TechnologyDto;
import com.skillgraph.exception.DatabaseException;
import com.skillgraph.exception.ResourceNotFoundException;
import com.skillgraph.repository.TechnologyRepository;
import org.neo4j.driver.exceptions.Neo4jException;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class TechnologyService {

    private final TechnologyRepository repo;

    public TechnologyService(TechnologyRepository repo) { this.repo = repo; }

    public List<TechnologyDto> getAll() {
        try { return repo.findAll(); }
        catch (Neo4jException ex) { throw new DatabaseException("Failed to load technologies", ex); }
    }

    public TechnologyDto getById(String id) {
        try {
            return repo.findById(id)
                    .orElseThrow(() -> new ResourceNotFoundException("Technology", id));
        } catch (Neo4jException ex) {
            throw new DatabaseException("Failed to load technology " + id, ex);
        }
    }
}
