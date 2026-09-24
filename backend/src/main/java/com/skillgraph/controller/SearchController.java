package com.skillgraph.controller;

import com.skillgraph.dto.ApiResponse;
import com.skillgraph.dto.SearchResultDto;
import com.skillgraph.service.SearchService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/search")
public class SearchController {

    private final SearchService service;

    public SearchController(SearchService service) { this.service = service; }

    @GetMapping
    public ResponseEntity<ApiResponse<List<SearchResultDto>>> search(@RequestParam String q) {
        return ResponseEntity.ok(ApiResponse.success(service.search(q), "Search completed"));
    }
}
