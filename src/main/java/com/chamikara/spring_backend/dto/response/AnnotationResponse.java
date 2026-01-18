package com.chamikara.spring_backend.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AnnotationResponse {
    
    private Long id;
    private Long inspectionId;
    private String annotationId;
    private Double x;
    private Double y;
    private Double w;
    private Double h;
    private Double confidence;
    private String severity;
    private String classification;
    private String comment;
    private String source;
    private Boolean deleted;
    private String userId;
    private String createdAt;
    private String updatedAt;
}
