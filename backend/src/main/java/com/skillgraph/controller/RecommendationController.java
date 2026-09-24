package com.skillgraph.controller;

import com.skillgraph.dto.ApiResponse;
import com.skillgraph.dto.RecommendationDto;
import com.skillgraph.service.RecommendationService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/recommendations")
public class RecommendationController {

    private final RecommendationService service;

    public RecommendationController(RecommendationService service) { this.service = service; }

    /** Ranked job-role recommendations for a candidate */
    @GetMapping("/{userId}")
    public ResponseEntity<ApiResponse<List<RecommendationDto>>> recommend(@PathVariable String userId) {
        return ResponseEntity.ok(ApiResponse.success(service.recommendRoles(userId), "Recommendations loaded"));
    }

    /** Skill-gap analysis: what skills does the candidate still need for a specific role */
    @GetMapping("/{userId}/gap/{roleId}")
    public ResponseEntity<ApiResponse<List<RecommendationDto>>> gap(
            @PathVariable String userId,
            @PathVariable String roleId) {
        return ResponseEntity.ok(ApiResponse.success(service.skillGap(userId, roleId), "Skill gap loaded"));
    }
}
