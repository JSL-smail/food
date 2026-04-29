package com.example.foodproject.service;

import java.util.List;

public interface DocumentParserService {

    String parseDocument(String filePath, String fileType);

    List<String> chunkText(String content, int chunkSize, int overlap);
}