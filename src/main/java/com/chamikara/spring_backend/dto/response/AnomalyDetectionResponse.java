package com.chamikara.spring_backend.dto.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * Response DTO for FastAPI anomaly detection service
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AnomalyDetectionResponse {
    
    private String requestId;
    private String transformerId;
    private String timestamp;
    private String imageLevelLabel;
    private Integer anomalyCount;
    private List<DetectedAnomaly> anomalies;
    private DetectionMetrics metrics;
    private OverlayImage overlayImage;
    
    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    public static class DetectedAnomaly {
        private String id;
        private BoundingBox bbox;
        private Double confidence;
        private String severity;
        private String classification;
        private Integer area;
    }
    
    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    public static class BoundingBox {
        private Integer x;
        private Integer y;
        private Integer width;
        private Integer height;
    }
    
    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    public static class DetectionMetrics {
        private Double meanSsim;
        private String warpModel;
        private Double thresholdPotential;
        private Double thresholdFault;
        private Double basePotential;
        private Double baseFault;
        private Double sliderPercent;
        private Double scaleApplied;
        private String thresholdSource;
        private String ratio;
    }
    
    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    public static class OverlayImage {
        private String filename;
        private Long size;
        private String path;
    }
}
