package com.example.foodproject.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.example.foodproject.entity.KbDocument;
import com.example.foodproject.entity.KbDocumentChunk;
import com.example.foodproject.entity.KnowledgeBase;
import com.example.foodproject.exception.BusinessException;
import com.example.foodproject.mapper.KbDocumentChunkMapper;
import com.example.foodproject.mapper.KbDocumentMapper;
import com.example.foodproject.mapper.KnowledgeBaseMapper;
import com.example.foodproject.service.DocumentParserService;
import com.example.foodproject.service.KbDocumentService;
import org.springframework.ai.document.Document;
import org.springframework.ai.vectorstore.VectorStore;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Service
public class KbDocumentServiceImpl implements KbDocumentService {

    private final KbDocumentMapper kbDocumentMapper;
    private final KbDocumentChunkMapper chunkMapper;
    private final KnowledgeBaseMapper knowledgeBaseMapper;
    private final DocumentParserService documentParserService;
    private final VectorStore vectorStore;

    public KbDocumentServiceImpl(KbDocumentMapper kbDocumentMapper,
                                  KbDocumentChunkMapper chunkMapper,
                                  KnowledgeBaseMapper knowledgeBaseMapper,
                                  DocumentParserService documentParserService,
                                  VectorStore vectorStore) {
        this.kbDocumentMapper = kbDocumentMapper;
        this.chunkMapper = chunkMapper;
        this.knowledgeBaseMapper = knowledgeBaseMapper;
        this.documentParserService = documentParserService;
        this.vectorStore = vectorStore;
    }

    @Override
    @Transactional
    public KbDocument uploadDocument(KbDocument document, String filePath) {
        KnowledgeBase kb = knowledgeBaseMapper.selectById(document.getKbId());
        if (kb == null) {
            throw new BusinessException("知识库不存在");
        }

        String content = documentParserService.parseDocument(filePath, document.getFileType());
        document.setContent(content);
        document.setStatus("pending");
        document.setCreatedAt(LocalDateTime.now());
        document.setUpdatedAt(LocalDateTime.now());

        kbDocumentMapper.insert(document);

        processDocument(document.getId());

        LambdaQueryWrapper<KnowledgeBase> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(KnowledgeBase::getId, document.getKbId());
        KnowledgeBase updateKb = new KnowledgeBase();
        updateKb.setDocumentCount(kb.getDocumentCount() + 1);
        knowledgeBaseMapper.update(updateKb, wrapper);

        return document;
    }

    @Override
    public KbDocument getById(Long id) {
        KbDocument document = kbDocumentMapper.selectById(id);
        if (document == null) {
            throw new BusinessException("文档不存在");
        }
        return document;
    }

    @Override
    public void delete(Long id) {
        KbDocument document = kbDocumentMapper.selectById(id);
        if (document == null) {
            throw new BusinessException("文档不存在");
        }

        LambdaQueryWrapper<KbDocumentChunk> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(KbDocumentChunk::getDocumentId, id);
        chunkMapper.delete(wrapper);

        // 注意：这里需要从VectorStore中删除，通常需要metadata过滤
        // vectorStore.delete(Collections.singletonList(id.toString())); 
        // 实际应用中可能需要更复杂的删除逻辑

        kbDocumentMapper.deleteById(id);
    }

    @Override
    @Transactional
    public void processDocument(Long documentId) {
        KbDocument document = kbDocumentMapper.selectById(documentId);
        if (document == null) {
            throw new BusinessException("文档不存在");
        }

        List<String> chunks = documentParserService.chunkText(document.getContent(), 500, 50);
        List<Document> aiDocuments = new ArrayList<>();

        for (int i = 0; i < chunks.size(); i++) {
            String content = chunks.get(i);
            
            // 保存到关系型数据库
            KbDocumentChunk chunk = new KbDocumentChunk();
            chunk.setDocumentId(documentId);
            chunk.setContent(content);
            chunk.setChunkIndex(i);
            chunk.setMetadata("{\"kb_id\":" + document.getKbId() + "}");
            chunk.setCreatedAt(LocalDateTime.now());
            chunkMapper.insert(chunk);

            // 准备保存到VectorStore
            Document aiDoc = new Document(content, Map.of(
                "kb_id", document.getKbId(),
                "doc_id", documentId,
                "chunk_index", i
            ));
            aiDocuments.add(aiDoc);
        }

        // 批量保存到VectorStore (Milvus)
        vectorStore.add(aiDocuments);

        document.setChunkCount(chunks.size());
        document.setStatus("processed");
        document.setUpdatedAt(LocalDateTime.now());
        kbDocumentMapper.updateById(document);
    }
}