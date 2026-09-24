package com.skillgraph.controller;

import com.skillgraph.dto.ApiResponse;
import com.skillgraph.dto.TechnologyDto;
import com.skillgraph.service.TechnologyService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/technologies")
public class TechnologyController {

    private final TechnologyService service;

    public TechnologyController(TechnologyService service) { this.service = service; }

    @GetMapping
    public ResponseEntity<ApiResponse<List<TechnologyDto>>> list() {
        return ResponseEntity.ok(ApiResponse.success(service.getAll(), "Technologies loaded"));
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<TechnologyDto>> get(@PathVariable String id) {
        return ResponseEntity.ok(ApiResponse.success(service.getById(id), "Technology loaded"));
    }
}
