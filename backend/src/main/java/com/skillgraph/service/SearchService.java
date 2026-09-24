package com.skillgraph.service;

import com.skillgraph.dto.SearchResultDto;
import com.skillgraph.exception.DatabaseException;
import com.skillgraph.repository.SearchRepository;
import org.neo4j.driver.exceptions.Neo4jException;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class SearchService {

    private final SearchRepository repo;

    public SearchService(SearchRepository repo) { this.repo = repo; }

    public List<SearchResultDto> search(String term) {
        if (term == null || term.isBlank()) {
            throw new IllegalArgumentException("Search term must not be blank");
        }
        if (term.length() < 2) {
            throw new IllegalArgumentException("Search term must be at least 2 characters");
        }
        try { return repo.search(term.trim()); }
        catch (Neo4jException ex) { throw new DatabaseException("Search failed", ex); }
    }
}
