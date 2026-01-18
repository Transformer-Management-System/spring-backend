package com.chamikara.spring_backend.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class TransformerResponse {
    
    private Long id;
    private String number;
    private String pole;
    private String region;
    private String type;
    private String baselineImage;
    private String baselineUploadDate;
    private String weather;
    private String location;
    private Integer inspectionCount;
}
