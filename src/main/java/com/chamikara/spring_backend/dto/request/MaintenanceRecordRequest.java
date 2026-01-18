package com.chamikara.spring_backend.dto.request;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class MaintenanceRecordRequest {
    
    @NotNull(message = "Transformer ID is required")
    private Long transformerId;
    
    private Long inspectionId;
    
    private String engineerName;
    
    private String status;
    
    private String readings;
    
    private String recommendedAction;
    
    private String notes;
    
    private String annotatedImage;
    
    private String anomalies;
    
    private String location;
}
