package com.chamikara.spring_backend.controller;

import com.chamikara.spring_backend.dto.response.ApiResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDateTime;
import java.util.Map;

@RestController
@RequestMapping("/")
@Slf4j
@CrossOrigin(origins = "*")
public class HealthController {
    
    @GetMapping
    public ResponseEntity<ApiResponse<Map<String, Object>>> root() {
        Map<String, Object> info = Map.of(
                "service", "Thermal Inspection Automation System API",
                "version", "1.0.0",
                "status", "running",
                "timestamp", LocalDateTime.now().toString()
        );
        return ResponseEntity.ok(ApiResponse.success("Welcome to the API", info));
    }
    
    @GetMapping("/health")
    public ResponseEntity<ApiResponse<Map<String, String>>> health() {
        log.debug("Health check endpoint called");
        Map<String, String> health = Map.of(
                "status", "UP",
                "database", "connected"
        );
        return ResponseEntity.ok(ApiResponse.success("Service is healthy", health));
    }
    
    @GetMapping("/api/info")
    public ResponseEntity<ApiResponse<Map<String, Object>>> apiInfo() {
        Map<String, Object> info = Map.of(
                "name", "Thermal Inspection Automation System",
                "description", "Backend API for managing transformer inspections, anomaly detection, and maintenance records",
                "version", "1.0.0",
                "endpoints", Map.of(
                        "transformers", "/transformers",
                        "inspections", "/inspections",
                        "annotations", "/annotations",
                        "annotationLogs", "/annotation-logs",
                        "records", "/records",
                        "anomalyDetection", "/anomaly-detection"
                )
        );
        return ResponseEntity.ok(ApiResponse.success("API information", info));
    }
}
