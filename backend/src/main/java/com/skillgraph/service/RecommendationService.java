package com.skillgraph.service;

import com.skillgraph.dto.RecommendationDto;
import com.skillgraph.exception.DatabaseException;
import com.skillgraph.exception.ResourceNotFoundException;
import com.skillgraph.repository.RecommendationRepository;
import com.skillgraph.repository.UserRepository;
import org.neo4j.driver.exceptions.Neo4jException;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class RecommendationService {

    private final RecommendationRepository repo;
    private final UserRepository userRepo;

    public RecommendationService(RecommendationRepository repo, UserRepository userRepo) {
        this.repo     = repo;
        this.userRepo = userRepo;
    }

    public List<RecommendationDto> recommendRoles(String userId) {
        userRepo.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("Candidate", userId));
        try { return repo.recommendRoles(userId); }
        catch (Neo4jException ex) { throw new DatabaseException("Failed to compute recommendations", ex); }
    }

    public List<RecommendationDto> skillGap(String userId, String roleId) {
        userRepo.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("Candidate", userId));
        try { return repo.skillGap(userId, roleId); }
        catch (Neo4jException ex) { throw new DatabaseException("Failed to compute skill gap", ex); }
    }
}
