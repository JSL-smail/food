package com.example.foodproject.service;

import com.example.foodproject.entity.KnowledgeBase;

public interface KnowledgeBaseService {

    KnowledgeBase create(KnowledgeBase knowledgeBase);

    KnowledgeBase update(KnowledgeBase knowledgeBase);

    void delete(Long id);

    KnowledgeBase getById(Long id);
}