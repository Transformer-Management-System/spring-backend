package com.chamikara.spring_backend.dto.request;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class SaveAnnotationsRequest {
    
    @NotNull(message = "Annotations list is required")
    @Valid
    private List<AnnotationRequest> annotations;
    
    private String annotatedImage;
    
    private String userId;
}
