package com.example.foodproject.service.impl;

import com.example.foodproject.service.DocumentParserService;
import org.apache.pdfbox.Loader;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.text.PDFTextStripper;
import org.apache.poi.xwpf.usermodel.XWPFDocument;
import org.apache.poi.xwpf.usermodel.XWPFParagraph;
import org.springframework.stereotype.Service;

import java.io.FileInputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

@Service
public class DocumentParserServiceImpl implements DocumentParserService {

    @Override
    public String parseDocument(String filePath, String fileType) {
        try {
            return switch (fileType.toLowerCase()) {
                case "pdf" -> parsePdf(filePath);
                case "docx", "doc" -> parseWord(filePath);
                case "txt" -> parseTxt(filePath);
                default -> throw new IllegalArgumentException("Unsupported file type: " + fileType);
            };
        } catch (IOException e) {
            throw new RuntimeException("Failed to parse document: " + e.getMessage(), e);
        }
    }

    @Override
    public List<String> chunkText(String content, int chunkSize, int overlap) {
        List<String> chunks = new ArrayList<>();
        if (content == null || content.isEmpty()) {
            return chunks;
        }

        String[] sentences = content.split("[。！？；\n]");
        StringBuilder currentChunk = new StringBuilder();
        int currentLength = 0;

        for (String sentence : sentences) {
            int sentenceLength = sentence.length();
            if (currentLength + sentenceLength > chunkSize && currentLength > 0) {
                chunks.add(currentChunk.toString().trim());
                String overlapText = currentChunk.toString();
                currentChunk = new StringBuilder();

                int overlapStart = Math.max(0, overlapText.length() - overlap);
                int overlapEnd = Math.min(overlapText.length(), overlapStart + overlap);
                if (overlapEnd > overlapStart) {
                    currentChunk.append(overlapText, overlapStart, overlapEnd);
                }
                currentLength = currentChunk.length();
            }
            currentChunk.append(sentence).append("。");
            currentLength += sentenceLength + 1;
        }

        if (currentChunk.length() > 0) {
            chunks.add(currentChunk.toString().trim());
        }

        return chunks;
    }

    private String parsePdf(String filePath) throws IOException {
        try (PDDocument document = Loader.loadPDF(Files.readAllBytes(Path.of(filePath)))) {
            PDFTextStripper stripper = new PDFTextStripper();
            return stripper.getText(document);
        }
    }

    private String parseWord(String filePath) throws IOException {
        try (FileInputStream fis = new FileInputStream(filePath);
             XWPFDocument document = new XWPFDocument(fis)) {
            StringBuilder text = new StringBuilder();
            for (XWPFParagraph paragraph : document.getParagraphs()) {
                text.append(paragraph.getText()).append("\n");
            }
            return text.toString();
        }
    }

    private String parseTxt(String filePath) throws IOException {
        return Files.readString(Path.of(filePath));
    }
}