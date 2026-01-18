package com.chamikara.spring_backend.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AnnotationLogResponse {
    
    private Long id;
    private Long inspectionId;
    private Long transformerId;
    private String transformerNumber;
    private String imageId;
    private String actionType;
    private String annotationData;
    private String aiPrediction;
    private String userAnnotation;
    private String userId;
    private String timestamp;
    private String notes;
}
