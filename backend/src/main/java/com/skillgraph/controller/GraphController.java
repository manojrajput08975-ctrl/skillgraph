package com.skillgraph.controller;

import com.skillgraph.dto.ApiResponse;
import com.skillgraph.dto.GraphDto;
import com.skillgraph.service.GraphService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/graph")
public class GraphController {

    private final GraphService service;

    public GraphController(GraphService service) { this.service = service; }

    /** Full career-path graph for force-graph visualisation */
    @GetMapping("/career-path")
    public ResponseEntity<ApiResponse<GraphDto>> careerPath() {
        return ResponseEntity.ok(ApiResponse.success(service.careerPathGraph(), "Career path graph loaded"));
    }

    /** 4-hop traversal: User→Project→Technology→Skill→JobRole */
    @GetMapping("/traversal")
    public ResponseEntity<ApiResponse<List<Map<String, String>>>> traversal() {
        return ResponseEntity.ok(ApiResponse.success(service.multiHopTraversal(), "Traversal completed"));
    }

    /** Graph neighbourhood (up to 2 hops) around any node */
    @GetMapping("/neighbourhood/{nodeId}")
    public ResponseEntity<ApiResponse<GraphDto>> neighbourhood(@PathVariable String nodeId) {
        return ResponseEntity.ok(ApiResponse.success(service.neighbourhood(nodeId), "Neighbourhood loaded"));
    }
}
