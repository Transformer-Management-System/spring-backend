package com.chamikara.spring_backend.service;

import com.chamikara.spring_backend.dto.response.AnomalyDetectionResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.http.MediaType;
import org.springframework.http.client.MultipartBodyBuilder;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import java.time.Duration;
import java.util.Base64;

/**
 * Service for communicating with the FastAPI anomaly detection microservice
 */
@Service
@Slf4j
public class AnomalyDetectionService {
    
    private final WebClient webClient;
    private final long timeout;
    
    public AnomalyDetectionService(
            @Value("${fastapi.service.url}") String fastApiUrl,
            @Value("${fastapi.service.timeout:60000}") long timeout) {
        this.webClient = WebClient.builder()
                .baseUrl(fastApiUrl)
                .build();
        this.timeout = timeout;
    }
    
    /**
     * Detect anomalies by comparing baseline and maintenance images
     *
     * @param transformerId The transformer identifier
     * @param baselineImage Base64 encoded baseline image (with or without data URI prefix)
     * @param maintenanceImage Base64 encoded maintenance image (with or without data URI prefix)
     * @param sliderPercent Optional threshold adjustment percentage
     * @return AnomalyDetectionResponse with detection results
     */
    public AnomalyDetectionResponse detectAnomalies(
            String transformerId,
            String baselineImage,
            String maintenanceImage,
            Double sliderPercent) {
        
        log.info("Calling anomaly detection service for transformer: {}", transformerId);
        
        try {
            // Convert base64 to bytes
            byte[] baselineBytes = decodeBase64Image(baselineImage);
            byte[] maintenanceBytes = decodeBase64Image(maintenanceImage);
            
            // Build multipart request
            MultipartBodyBuilder builder = new MultipartBodyBuilder();
            builder.part("baseline", new ByteArrayResource(baselineBytes) {
                @Override
                public String getFilename() {
                    return "baseline.png";
                }
            }).contentType(MediaType.IMAGE_PNG);
            
            builder.part("maintenance", new ByteArrayResource(maintenanceBytes) {
                @Override
                public String getFilename() {
                    return "maintenance.png";
                }
            }).contentType(MediaType.IMAGE_PNG);
            
            builder.part("transformer_id", transformerId);
            
            if (sliderPercent != null) {
                builder.part("slider_percent", sliderPercent.toString());
            }
            
            // Make request to FastAPI
            AnomalyDetectionResponse response = webClient.post()
                    .uri("/api/v1/detect")
                    .contentType(MediaType.MULTIPART_FORM_DATA)
                    .body(BodyInserters.fromMultipartData(builder.build()))
                    .retrieve()
                    .bodyToMono(AnomalyDetectionResponse.class)
                    .timeout(Duration.ofMillis(timeout))
                    .block();
            
            log.info("Anomaly detection completed for transformer: {}. Found {} anomalies", 
                    transformerId, response != null ? response.getAnomalyCount() : 0);
            
            return response;
            
        } catch (Exception e) {
            log.error("Failed to detect anomalies for transformer: {}", transformerId, e);
            throw new RuntimeException("Anomaly detection service call failed: " + e.getMessage(), e);
        }
    }
    
    /**
     * Asynchronous version of anomaly detection
     */
    public Mono<AnomalyDetectionResponse> detectAnomaliesAsync(
            String transformerId,
            String baselineImage,
            String maintenanceImage,
            Double sliderPercent) {
        
        log.info("Calling anomaly detection service asynchronously for transformer: {}", transformerId);
        
        try {
            byte[] baselineBytes = decodeBase64Image(baselineImage);
            byte[] maintenanceBytes = decodeBase64Image(maintenanceImage);
            
            MultipartBodyBuilder builder = new MultipartBodyBuilder();
            builder.part("baseline", new ByteArrayResource(baselineBytes) {
                @Override
                public String getFilename() {
                    return "baseline.png";
                }
            }).contentType(MediaType.IMAGE_PNG);
            
            builder.part("maintenance", new ByteArrayResource(maintenanceBytes) {
                @Override
                public String getFilename() {
                    return "maintenance.png";
                }
            }).contentType(MediaType.IMAGE_PNG);
            
            builder.part("transformer_id", transformerId);
            
            if (sliderPercent != null) {
                builder.part("slider_percent", sliderPercent.toString());
            }
            
            return webClient.post()
                    .uri("/api/v1/detect")
                    .contentType(MediaType.MULTIPART_FORM_DATA)
                    .body(BodyInserters.fromMultipartData(builder.build()))
                    .retrieve()
                    .bodyToMono(AnomalyDetectionResponse.class)
                    .timeout(Duration.ofMillis(timeout));
            
        } catch (Exception e) {
            log.error("Failed to prepare anomaly detection request for transformer: {}", transformerId, e);
            return Mono.error(new RuntimeException("Failed to prepare anomaly detection request", e));
        }
    }
    
    /**
     * Check if the FastAPI service is healthy
     */
    public boolean isServiceHealthy() {
        try {
            String response = webClient.get()
                    .uri("/health")
                    .retrieve()
                    .bodyToMono(String.class)
                    .timeout(Duration.ofSeconds(5))
                    .block();
            return response != null && response.contains("healthy");
        } catch (Exception e) {
            log.warn("FastAPI service health check failed", e);
            return false;
        }
    }
    
    /**
     * Decode base64 image string (handles data URI prefix)
     */
    private byte[] decodeBase64Image(String base64Image) {
        if (base64Image == null || base64Image.isEmpty()) {
            throw new IllegalArgumentException("Image data cannot be empty");
        }
        
        String base64Data = base64Image;
        
        // Remove data URI prefix if present
        if (base64Image.contains(",")) {
            base64Data = base64Image.substring(base64Image.indexOf(",") + 1);
        }
        
        return Base64.getDecoder().decode(base64Data);
    }
}
