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
public class InspectionRequest {
    
    @NotNull(message = "Transformer ID is required")
    private Long transformerId;
    
    private String date;
    
    private String inspectedDate;
    
    private String inspector;
    
    private String notes;
    
    private String status;
    
    private String maintenanceImage;
    
    private String maintenanceUploadDate;
    
    private String maintenanceWeather;
    
    private String annotatedImage;
    
    private String anomalies;
    
    private String progressStatus;
}
