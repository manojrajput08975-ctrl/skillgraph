package com.skillgraph.controller;

import com.skillgraph.dto.ApiResponse;
import com.skillgraph.dto.DashboardDto;
import com.skillgraph.service.DashboardService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api")
public class DashboardController {

    private final DashboardService service;

    public DashboardController(DashboardService service) { this.service = service; }

    @GetMapping("/dashboard")
    public ResponseEntity<ApiResponse<DashboardDto>> dashboard() {
        return ResponseEntity.ok(ApiResponse.success(service.getCounts(), "Dashboard loaded"));
    }
}
