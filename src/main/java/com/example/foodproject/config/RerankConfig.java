package com.example.foodproject.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestTemplate;

@Configuration
public class RerankConfig {

    @Value("${rerank.base-url}")
    private String baseUrl;

    @Value("${rerank.model}")
    private String model;

    @Bean
    public RestTemplate rerankRestTemplate() {
        return new RestTemplate();
    }

    public String getBaseUrl() {
        return baseUrl;
    }

    public String getModel() {
        return model;
    }
}