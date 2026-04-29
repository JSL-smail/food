package com.example.foodproject.service.impl;

import com.example.foodproject.config.RerankConfig;
import com.example.foodproject.service.RetrievalService;
import org.springframework.ai.document.Document;
import org.springframework.ai.vectorstore.SearchRequest;
import org.springframework.ai.vectorstore.VectorStore;
import org.springframework.ai.vectorstore.filter.Filter;
import org.springframework.ai.vectorstore.filter.FilterExpressionBuilder;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.*;
import java.util.stream.Collectors;

@Service
public class RetrievalServiceImpl implements RetrievalService {

    private final VectorStore vectorStore;
    private final RerankConfig rerankConfig;
    private final RestTemplate rerankRestTemplate;

    public RetrievalServiceImpl(VectorStore vectorStore, RerankConfig rerankConfig, RestTemplate rerankRestTemplate) {
        this.vectorStore = vectorStore;
        this.rerankConfig = rerankConfig;
        this.rerankRestTemplate = rerankRestTemplate;
    }

    @Override
    public List<String> retrieve(String query, int topK, Long knowledgeBaseId) {
        SearchRequest searchRequest = SearchRequest.builder()
                .query(query)
                .topK(topK)
                .filterExpression(new FilterExpressionBuilder().eq("kb_id", knowledgeBaseId).build())
                .build();

        List<Document> resultDocs = vectorStore.similaritySearch(searchRequest);
        return resultDocs.stream()
                .map(Document::getText)
                .collect(Collectors.toList());
    }

    @Override
    public List<String> rerank(String query, List<String> documents, int topN) {
        if (documents == null || documents.isEmpty()) {
            return new ArrayList<>();
        }

        try {
            String url = rerankConfig.getBaseUrl() + "/v1/rerank";
            Map<String, Object> requestBody = new HashMap<>();
            requestBody.put("model", rerankConfig.getModel());
            requestBody.put("query", query);
            requestBody.put("documents", documents);
            requestBody.put("top_n", topN);

            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);

            HttpEntity<Map<String, Object>> request = new HttpEntity<>(requestBody, headers);
            ResponseEntity<Map> response = rerankRestTemplate.postForEntity(url, request, Map.class);

            if (response.getStatusCode().is2xxSuccessful() && response.getBody() != null) {
                List<Map<String, Object>> results = (List<Map<String, Object>>) response.getBody().get("results");
                List<String> rerankedDocs = new ArrayList<>();
                for (Map<String, Object> result : results) {
                    int index = ((Number) result.get("index")).intValue();
                    rerankedDocs.add(documents.get(index));
                }
                return rerankedDocs;
            }
        } catch (Exception e) {
            System.err.println("Rerank failed, returning original order: " + e.getMessage());
        }
        return documents.subList(0, Math.min(topN, documents.size()));
    }
}