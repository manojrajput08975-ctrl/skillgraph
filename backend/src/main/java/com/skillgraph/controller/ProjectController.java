package com.skillgraph.controller;

import com.skillgraph.dto.ApiResponse;
import com.skillgraph.dto.ProjectDto;
import com.skillgraph.service.ProjectService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/projects")
public class ProjectController {

    private final ProjectService service;

    public ProjectController(ProjectService service) { this.service = service; }

    @GetMapping
    public ResponseEntity<ApiResponse<List<ProjectDto>>> list() {
        return ResponseEntity.ok(ApiResponse.success(service.getAll(), "Projects loaded"));
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<ProjectDto>> get(@PathVariable String id) {
        return ResponseEntity.ok(ApiResponse.success(service.getById(id), "Project loaded"));
    }
}
