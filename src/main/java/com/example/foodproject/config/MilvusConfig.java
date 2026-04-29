package com.example.foodproject.config;

import org.springframework.ai.embedding.EmbeddingModel;
import org.springframework.ai.vectorstore.milvus.MilvusVectorStore;
import org.springframework.ai.vectorstore.milvus.MilvusVectorStoreConfig;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class MilvusConfig {

    @Value("${spring.ai.milvus.url}")
    private String host;

    @Value("${spring.ai.milvus.port}")
    private Integer port;

    @Value("${spring.ai.milvus.user}")
    private String user;

    @Value("${spring.ai.milvus.password}")
    private String password;

    @Value("${spring.ai.milvus.collection-name}")
    private String collectionName;

    @Bean
    public MilvusVectorStore milvusVectorStore(EmbeddingModel embeddingModel) {
        MilvusVectorStoreConfig config = MilvusVectorStoreConfig.builder()
                .withHost(host)
                .withPort(port)
                .withCollectionName(collectionName)
                .build();

        return new MilvusVectorStore(config, embeddingModel);
    }
}