package com.example.foodproject.service;

import java.util.List;

public interface RetrievalService {

    List<String> retrieve(String query, int topK, Long knowledgeBaseId);

    List<String> rerank(String query, List<String> documents, int topN);
}