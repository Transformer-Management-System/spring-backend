package com.chamikara.spring_backend.controller;

import com.chamikara.spring_backend.dto.response.AnomalyDetectionResponse;
import com.chamikara.spring_backend.dto.response.ApiResponse;
import com.chamikara.spring_backend.service.AnomalyDetectionService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/anomaly-detection")
@RequiredArgsConstructor
@Slf4j
@CrossOrigin(origins = "*")
public class AnomalyDetectionController {
    
    private final AnomalyDetectionService anomalyDetectionService;
    
    /**
     * Detect anomalies by comparing baseline and maintenance images
     * 
     * Request body should contain:
     * - transformerId: String (required)
     * - baselineImage: String - Base64 encoded image (required)
     * - maintenanceImage: String - Base64 encoded image (required)
     * - sliderPercent: Double - Optional threshold adjustment
     */
    @PostMapping("/detect")
    public ResponseEntity<ApiResponse<AnomalyDetectionResponse>> detectAnomalies(
            @RequestBody Map<String, Object> request) {
        
        String transformerId = (String) request.get("transformerId");
        String baselineImage = (String) request.get("baselineImage");
        String maintenanceImage = (String) request.get("maintenanceImage");
        Double sliderPercent = request.get("sliderPercent") != null 
                ? ((Number) request.get("sliderPercent")).doubleValue() 
                : null;
        
        log.info("POST /anomaly-detection/detect - Detecting anomalies for transformer: {}", transformerId);
        
        if (transformerId == null || transformerId.isEmpty()) {
            return ResponseEntity.badRequest()
                    .body(ApiResponse.error("Transformer ID is required"));
        }
        
        if (baselineImage == null || baselineImage.isEmpty()) {
            return ResponseEntity.badRequest()
                    .body(ApiResponse.error("Baseline image is required"));
        }
        
        if (maintenanceImage == null || maintenanceImage.isEmpty()) {
            return ResponseEntity.badRequest()
                    .body(ApiResponse.error("Maintenance image is required"));
        }
        
        AnomalyDetectionResponse response = anomalyDetectionService.detectAnomalies(
                transformerId, baselineImage, maintenanceImage, sliderPercent);
        
        return ResponseEntity.ok(ApiResponse.success("Anomaly detection completed", response));
    }
    
    /**
     * Health check endpoint for the FastAPI anomaly detection service
     */
    @GetMapping("/health")
    public ResponseEntity<ApiResponse<Map<String, Object>>> checkServiceHealth() {
        log.info("GET /anomaly-detection/health - Checking FastAPI service health");
        
        boolean isHealthy = anomalyDetectionService.isServiceHealthy();
        
        Map<String, Object> healthStatus = Map.of(
                "fastApiService", isHealthy ? "healthy" : "unhealthy",
                "status", isHealthy ? "All services operational" : "FastAPI service is down"
        );
        
        if (isHealthy) {
            return ResponseEntity.ok(ApiResponse.success("Service health check passed", healthStatus));
        } else {
            return ResponseEntity.ok(ApiResponse.error("FastAPI service is not available"));
        }
    }
}
