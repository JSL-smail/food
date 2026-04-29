package com.example.foodproject.service;

import com.example.foodproject.entity.KbDocument;

public interface KbDocumentService {

    KbDocument uploadDocument(KbDocument document, String filePath);

    KbDocument getById(Long id);

    void delete(Long id);

    void processDocument(Long documentId);
}