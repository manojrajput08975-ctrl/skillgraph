package com.skillgraph.service;

import com.skillgraph.dto.SkillDto;
import com.skillgraph.exception.DatabaseException;
import com.skillgraph.exception.ResourceNotFoundException;
import com.skillgraph.repository.SkillRepository;
import org.neo4j.driver.exceptions.Neo4jException;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class SkillService {

    private final SkillRepository repo;

    public SkillService(SkillRepository repo) { this.repo = repo; }

    public List<SkillDto> getAll() {
        try { return repo.findAll(); }
        catch (Neo4jException ex) { throw new DatabaseException("Failed to load skills", ex); }
    }

    public SkillDto getById(String id) {
        try {
            return repo.findById(id)
                    .orElseThrow(() -> new ResourceNotFoundException("Skill", id));
        } catch (Neo4jException ex) {
            throw new DatabaseException("Failed to load skill " + id, ex);
        }
    }
}
