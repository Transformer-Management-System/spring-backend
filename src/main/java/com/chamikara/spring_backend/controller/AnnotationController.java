package com.chamikara.spring_backend.controller;

import com.chamikara.spring_backend.dto.request.SaveAnnotationsRequest;
import com.chamikara.spring_backend.dto.response.ApiResponse;
import com.chamikara.spring_backend.dto.response.AnnotationResponse;
import com.chamikara.spring_backend.service.AnnotationService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/annotations")
@RequiredArgsConstructor
@Slf4j
@CrossOrigin(origins = "*")
public class AnnotationController {
    
    private final AnnotationService annotationService;
    
    @GetMapping("/{inspectionId}")
    public ResponseEntity<ApiResponse<List<AnnotationResponse>>> getAnnotationsByInspectionId(
            @PathVariable Long inspectionId) {
        log.info("GET /annotations/{} - Fetching annotations for inspection", inspectionId);
        List<AnnotationResponse> annotations = annotationService.getAnnotationsByInspectionId(inspectionId);
        return ResponseEntity.ok(ApiResponse.success("Annotations retrieved successfully", annotations));
    }
    
    @PostMapping("/{inspectionId}")
    public ResponseEntity<ApiResponse<List<AnnotationResponse>>> saveAnnotations(
            @PathVariable Long inspectionId,
            @Valid @RequestBody SaveAnnotationsRequest request) {
        log.info("POST /annotations/{} - Saving annotations", inspectionId);
        List<AnnotationResponse> saved = annotationService.saveAnnotations(inspectionId, request);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ApiResponse.success("Annotations saved successfully", saved));
    }
}
