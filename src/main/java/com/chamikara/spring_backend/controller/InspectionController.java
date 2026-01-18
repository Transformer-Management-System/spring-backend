package com.chamikara.spring_backend.controller;

import com.chamikara.spring_backend.dto.request.InspectionRequest;
import com.chamikara.spring_backend.dto.response.ApiResponse;
import com.chamikara.spring_backend.dto.response.InspectionResponse;
import com.chamikara.spring_backend.service.InspectionService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/inspections")
@RequiredArgsConstructor
@Slf4j
@CrossOrigin(origins = "*")
public class InspectionController {
    
    private final InspectionService inspectionService;
    
    @GetMapping
    public ResponseEntity<ApiResponse<List<InspectionResponse>>> getAllInspections() {
        log.info("GET /inspections - Fetching all inspections");
        List<InspectionResponse> inspections = inspectionService.getAllInspections();
        return ResponseEntity.ok(ApiResponse.success("Inspections retrieved successfully", inspections));
    }
    
    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<InspectionResponse>> getInspectionById(@PathVariable Long id) {
        log.info("GET /inspections/{} - Fetching inspection", id);
        InspectionResponse inspection = inspectionService.getInspectionById(id);
        return ResponseEntity.ok(ApiResponse.success("Inspection retrieved successfully", inspection));
    }
    
    @GetMapping("/transformer/{transformerId}")
    public ResponseEntity<ApiResponse<List<InspectionResponse>>> getInspectionsByTransformerId(
            @PathVariable Long transformerId) {
        log.info("GET /inspections/transformer/{} - Fetching inspections", transformerId);
        List<InspectionResponse> inspections = inspectionService.getInspectionsByTransformerId(transformerId);
        return ResponseEntity.ok(ApiResponse.success("Inspections retrieved successfully", inspections));
    }
    
    @PostMapping
    public ResponseEntity<ApiResponse<InspectionResponse>> createInspection(
            @Valid @RequestBody InspectionRequest request) {
        log.info("POST /inspections - Creating new inspection for transformer: {}", request.getTransformerId());
        InspectionResponse created = inspectionService.createInspection(request);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ApiResponse.success("Inspection created successfully", created));
    }
    
    @PutMapping("/{id}")
    public ResponseEntity<ApiResponse<InspectionResponse>> updateInspection(
            @PathVariable Long id,
            @RequestBody InspectionRequest request) {
        log.info("PUT /inspections/{} - Updating inspection", id);
        InspectionResponse updated = inspectionService.updateInspection(id, request);
        return ResponseEntity.ok(ApiResponse.success("Inspection updated successfully", updated));
    }
    
    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<Void>> deleteInspection(@PathVariable Long id) {
        log.info("DELETE /inspections/{} - Deleting inspection", id);
        inspectionService.deleteInspection(id);
        return ResponseEntity.ok(ApiResponse.success("Inspection deleted successfully", null));
    }
}
