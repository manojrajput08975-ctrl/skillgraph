package com.skillgraph.controller;

import com.skillgraph.dto.ApiResponse;
import com.skillgraph.dto.UserDto;
import com.skillgraph.service.UserService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/candidates")
public class CandidateController {

    private final UserService service;

    public CandidateController(UserService service) { this.service = service; }

    @GetMapping
    public ResponseEntity<ApiResponse<List<UserDto>>> list() {
        return ResponseEntity.ok(ApiResponse.success(service.getAll(), "Candidates loaded"));
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<UserDto>> get(@PathVariable String id) {
        return ResponseEntity.ok(ApiResponse.success(service.getById(id), "Candidate loaded"));
    }
}
