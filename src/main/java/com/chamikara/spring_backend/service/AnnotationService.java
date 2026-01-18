package com.chamikara.spring_backend.service;

import com.chamikara.spring_backend.dto.request.AnnotationRequest;
import com.chamikara.spring_backend.dto.request.SaveAnnotationsRequest;
import com.chamikara.spring_backend.dto.response.AnnotationResponse;
import com.chamikara.spring_backend.entity.Annotation;
import com.chamikara.spring_backend.entity.AnnotationLog;
import com.chamikara.spring_backend.entity.Inspection;
import com.chamikara.spring_backend.exception.ResourceNotFoundException;
import com.chamikara.spring_backend.repository.AnnotationRepository;
import com.chamikara.spring_backend.repository.AnnotationLogRepository;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
@Transactional
public class AnnotationService {
    
    private final AnnotationRepository annotationRepository;
    private final AnnotationLogRepository annotationLogRepository;
    private final InspectionService inspectionService;
    private final ObjectMapper objectMapper;
    
    private static final DateTimeFormatter FORMATTER = DateTimeFormatter.ISO_LOCAL_DATE_TIME;
    
    public List<AnnotationResponse> getAnnotationsByInspectionId(Long inspectionId) {
        log.debug("Fetching annotations for inspection: {}", inspectionId);
        return annotationRepository.findByInspectionIdAndDeletedFalse(inspectionId).stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }
    
    public List<AnnotationResponse> saveAnnotations(Long inspectionId, SaveAnnotationsRequest request) {
        log.debug("Saving annotations for inspection: {}", inspectionId);
        
        Inspection inspection = inspectionService.getInspectionEntity(inspectionId);
        String now = LocalDateTime.now().format(FORMATTER);
        String userId = request.getUserId() != null ? request.getUserId() : "Admin";
        
        // Get existing annotations
        Map<String, Annotation> existingAnnotations = annotationRepository
                .findByInspectionId(inspectionId).stream()
                .collect(Collectors.toMap(Annotation::getAnnotationId, Function.identity()));
        
        List<Annotation> savedAnnotations = new ArrayList<>();
        
        for (AnnotationRequest annotationReq : request.getAnnotations()) {
            Annotation annotation;
            String actionType;
            String aiPrediction = null;
            
            if (existingAnnotations.containsKey(annotationReq.getAnnotationId())) {
                // Update existing annotation
                annotation = existingAnnotations.get(annotationReq.getAnnotationId());
                aiPrediction = serializeAnnotation(annotation); // Store original state
                actionType = Boolean.TRUE.equals(annotationReq.getDeleted()) ? "deleted" : "edited";
                
                updateAnnotationFields(annotation, annotationReq, now);
                existingAnnotations.remove(annotationReq.getAnnotationId());
            } else {
                // Create new annotation
                annotation = createNewAnnotation(inspection, annotationReq, now, userId);
                actionType = "ai".equals(annotationReq.getSource()) ? "ai_generated" : "added";
            }
            
            Annotation saved = annotationRepository.save(annotation);
            savedAnnotations.add(saved);
            
            // Create annotation log
            createAnnotationLog(inspection, saved, actionType, aiPrediction, userId, now);
        }
        
        // Update inspection with annotated image if provided
        if (request.getAnnotatedImage() != null) {
            inspection.setAnnotatedImage(request.getAnnotatedImage());
        }
        
        log.info("Saved {} annotations for inspection: {}", savedAnnotations.size(), inspectionId);
        
        return savedAnnotations.stream()
                .filter(a -> !Boolean.TRUE.equals(a.getDeleted()))
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }
    
    private Annotation createNewAnnotation(Inspection inspection, AnnotationRequest request, 
                                           String now, String userId) {
        return Annotation.builder()
                .inspection(inspection)
                .annotationId(request.getAnnotationId())
                .x(request.getX())
                .y(request.getY())
                .w(request.getW())
                .h(request.getH())
                .confidence(request.getConfidence())
                .severity(request.getSeverity())
                .classification(request.getClassification())
                .comment(request.getComment())
                .source(request.getSource())
                .deleted(Boolean.TRUE.equals(request.getDeleted()))
                .userId(userId)
                .createdAt(now)
                .updatedAt(now)
                .build();
    }
    
    private void updateAnnotationFields(Annotation annotation, AnnotationRequest request, String now) {
        annotation.setX(request.getX());
        annotation.setY(request.getY());
        annotation.setW(request.getW());
        annotation.setH(request.getH());
        annotation.setConfidence(request.getConfidence());
        annotation.setSeverity(request.getSeverity());
        annotation.setClassification(request.getClassification());
        annotation.setComment(request.getComment());
        annotation.setDeleted(Boolean.TRUE.equals(request.getDeleted()));
        annotation.setUpdatedAt(now);
    }
    
    private void createAnnotationLog(Inspection inspection, Annotation annotation, 
                                     String actionType, String aiPrediction, String userId, String now) {
        AnnotationLog log = AnnotationLog.builder()
                .inspection(inspection)
                .transformer(inspection.getTransformer())
                .imageId(inspection.getMaintenanceImage())
                .actionType(actionType)
                .annotationData(serializeAnnotation(annotation))
                .aiPrediction(aiPrediction)
                .userAnnotation(serializeAnnotation(annotation))
                .userId(userId)
                .timestamp(now)
                .build();
        
        annotationLogRepository.save(log);
    }
    
    private String serializeAnnotation(Annotation annotation) {
        try {
            return objectMapper.writeValueAsString(mapToResponse(annotation));
        } catch (JsonProcessingException e) {
            log.error("Failed to serialize annotation", e);
            return "{}";
        }
    }
    
    private AnnotationResponse mapToResponse(Annotation annotation) {
        return AnnotationResponse.builder()
                .id(annotation.getId())
                .inspectionId(annotation.getInspection().getId())
                .annotationId(annotation.getAnnotationId())
                .x(annotation.getX())
                .y(annotation.getY())
                .w(annotation.getW())
                .h(annotation.getH())
                .confidence(annotation.getConfidence())
                .severity(annotation.getSeverity())
                .classification(annotation.getClassification())
                .comment(annotation.getComment())
                .source(annotation.getSource())
                .deleted(annotation.getDeleted())
                .userId(annotation.getUserId())
                .createdAt(annotation.getCreatedAt())
                .updatedAt(annotation.getUpdatedAt())
                .build();
    }
}
