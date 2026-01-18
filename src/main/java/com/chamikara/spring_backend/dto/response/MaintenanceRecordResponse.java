package com.chamikara.spring_backend.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class MaintenanceRecordResponse {
    
    private Long id;
    private Long transformerId;
    private String transformerNumber;
    private Long inspectionId;
    private String recordTimestamp;
    private String engineerName;
    private String status;
    private String readings;
    private String recommendedAction;
    private String notes;
    private String annotatedImage;
    private String anomalies;
    private String location;
    private String createdAt;
    private String updatedAt;
}
