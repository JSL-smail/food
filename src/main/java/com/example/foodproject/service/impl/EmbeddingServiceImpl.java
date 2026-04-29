package com.example.foodproject.service.impl;

import com.example.foodproject.service.EmbeddingService;
import org.springframework.ai.embedding.EmbeddingModel;
import org.springframework.ai.embedding.EmbeddingRequest;
import org.springframework.ai.embedding.EmbeddingResponse;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class EmbeddingServiceImpl implements EmbeddingService {

    private final EmbeddingModel embeddingModel;

    public EmbeddingServiceImpl(EmbeddingModel embeddingModel) {
        this.embeddingModel = embeddingModel;
    }

    @Override
    public float[] embedText(String text) {
        EmbeddingRequest request = EmbeddingRequest.builder()
                .texts(List.of(text))
                .build();
        EmbeddingResponse response = embeddingModel.call(request);
        return response.getResult().getOutput();
    }

    @Override
    public List<float[]> embedTexts(List<String> texts) {
        EmbeddingRequest request = EmbeddingRequest.builder()
                .texts(texts)
                .build();
        EmbeddingResponse response = embeddingModel.call(request);
        return response.getResults().stream()
                .map(result -> result.getOutput())
                .collect(Collectors.toList());
    }
}