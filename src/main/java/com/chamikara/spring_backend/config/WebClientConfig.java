package com.chamikara.spring_backend.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.client.reactive.ReactorClientHttpConnector;
import org.springframework.web.reactive.function.client.ExchangeStrategies;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.netty.http.client.HttpClient;

import java.time.Duration;

@Configuration
public class WebClientConfig {
    
    @Value("${fastapi.service.url}")
    private String fastApiUrl;
    
    @Value("${fastapi.service.timeout:60000}")
    private long timeout;
    
    @Bean
    public WebClient webClient() {
        // Configure HTTP client with timeout
        HttpClient httpClient = HttpClient.create()
                .responseTimeout(Duration.ofMillis(timeout));
        
        // Configure exchange strategies for larger payloads (images)
        ExchangeStrategies strategies = ExchangeStrategies.builder()
                .codecs(configurer -> configurer
                        .defaultCodecs()
                        .maxInMemorySize(50 * 1024 * 1024)) // 50MB buffer for large images
                .build();
        
        return WebClient.builder()
                .baseUrl(fastApiUrl)
                .clientConnector(new ReactorClientHttpConnector(httpClient))
                .exchangeStrategies(strategies)
                .build();
    }
}
