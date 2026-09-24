package com.skillgraph.controller;

import com.skillgraph.dto.HealthDto;
import com.skillgraph.service.HealthService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * Exposes a health-check endpoint so operators (and the frontend) can
 * verify that the application is running and CognoDB is reachable.
 *
 * GET /api/health
 *   200 OK  → { "status": "UP",   "database": "CognoDB", "message": "..." }
 *   503     → { "status": "DOWN", "database": "CognoDB", "message": "..." }
 */
@RestController
public class HealthController {

    private final HealthService healthService;

    public HealthController(HealthService healthService) {
        this.healthService = healthService;
    }

    @GetMapping("/api/health")
    public ResponseEntity<HealthDto> health() {
        HealthDto dto = healthService.checkHealth();
        HttpStatus status = "UP".equals(dto.getStatus())
                ? HttpStatus.OK
                : HttpStatus.SERVICE_UNAVAILABLE;
        return ResponseEntity.status(status).body(dto);
    }

    /** Root endpoint — Render and load balancers hit "/" for health checks */
    @GetMapping("/")
    public ResponseEntity<HealthDto> root() {
        return health();
    }
}
