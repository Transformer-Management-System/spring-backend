package com.chamikara.spring_backend.dto.request;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class TransformerRequest {
    
    @NotBlank(message = "Transformer number is required")
    private String number;
    
    private String pole;
    
    private String region;
    
    private String type;
    
    private String baselineImage;
    
    private String baselineUploadDate;
    
    private String weather;
    
    private String location;
}
