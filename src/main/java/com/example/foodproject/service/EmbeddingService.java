package com.example.foodproject.service;

import java.util.List;

public interface EmbeddingService {

    float[] embedText(String text);

    List<float[]> embedTexts(List<String> texts);
}