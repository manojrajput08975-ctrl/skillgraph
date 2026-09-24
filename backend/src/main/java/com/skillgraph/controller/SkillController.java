package com.skillgraph.controller;

import com.skillgraph.dto.ApiResponse;
import com.skillgraph.dto.SkillDto;
import com.skillgraph.service.SkillService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/skills")
public class SkillController {

    private final SkillService service;

    public SkillController(SkillService service) { this.service = service; }

    @GetMapping
    public ResponseEntity<ApiResponse<List<SkillDto>>> list() {
        return ResponseEntity.ok(ApiResponse.success(service.getAll(), "Skills loaded"));
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<SkillDto>> get(@PathVariable String id) {
        return ResponseEntity.ok(ApiResponse.success(service.getById(id), "Skill loaded"));
    }
}
