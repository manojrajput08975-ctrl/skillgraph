package com.skillgraph.controller;

import com.skillgraph.dto.ApiResponse;
import com.skillgraph.dto.JobRoleDto;
import com.skillgraph.service.JobRoleService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/roles")
public class RoleController {

    private final JobRoleService service;

    public RoleController(JobRoleService service) { this.service = service; }

    @GetMapping
    public ResponseEntity<ApiResponse<List<JobRoleDto>>> list() {
        return ResponseEntity.ok(ApiResponse.success(service.getAll(), "Roles loaded"));
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<JobRoleDto>> get(@PathVariable String id) {
        return ResponseEntity.ok(ApiResponse.success(service.getById(id), "Role loaded"));
    }
}
