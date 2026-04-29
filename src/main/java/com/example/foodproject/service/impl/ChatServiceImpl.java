package com.example.foodproject.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.example.foodproject.entity.ChatMessage;
import com.example.foodproject.entity.ChatSession;
import com.example.foodproject.exception.BusinessException;
import com.example.foodproject.mapper.ChatMessageMapper;
import com.example.foodproject.mapper.ChatSessionMapper;
import com.example.foodproject.service.ChatService;
import com.example.foodproject.service.RetrievalService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.client.advisor.SimpleAdvisor;
import org.springframework.ai.chat.client.model.ChatMemoryAdvisor;
import org.springframework.ai.chat.client.model.UserMessage;
import org.springframework.ai.chat.memory.InMemoryChatMemory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Service
public class ChatServiceImpl implements ChatService {

    private final ChatSessionMapper sessionMapper;
    private final ChatMessageMapper messageMapper;
    private final RetrievalService retrievalService;
    private final ChatClient chatClient;
    private final ObjectMapper objectMapper;

    public ChatServiceImpl(ChatSessionMapper sessionMapper,
                          ChatMessageMapper messageMapper,
                          RetrievalService retrievalService,
                          ChatClient chatClient) {
        this.sessionMapper = sessionMapper;
        this.messageMapper = messageMapper;
        this.retrievalService = retrievalService;
        this.chatClient = chatClient;
        this.objectMapper = new ObjectMapper();
    }

    @Override
    @Transactional
    public ChatSession createSession(Long userId, String model, Long knowledgeBaseId) {
        ChatSession session = new ChatSession();
        session.setSessionId(UUID.randomUUID().toString());
        session.setUserId(userId);
        session.setTitle("新对话");
        session.setModel(model != null ? model : "qwen2.5:7b");
        session.setKnowledgeBaseId(knowledgeBaseId);
        session.setCreatedAt(LocalDateTime.now());
        session.setUpdatedAt(LocalDateTime.now());
        sessionMapper.insert(session);
        return session;
    }

    @Override
    @Transactional
    public ChatMessage sendMessage(String sessionId, String userMessage, Long knowledgeBaseId) {
        ChatSession session = getSessionBySessionId(sessionId);
        if (session == null) {
            throw new BusinessException("会话不存在");
        }

        List<String> contextDocs = null;
        if (knowledgeBaseId != null) {
            contextDocs = retrievalService.retrieve(userMessage, 5, knowledgeBaseId);
            if (contextDocs != null && !contextDocs.isEmpty()) {
                contextDocs = retrievalService.rerank(userMessage, contextDocs, 3);
            }
        }

        String contextPrompt = "";
        if (contextDocs != null && !contextDocs.isEmpty()) {
            contextPrompt = "\n\n参考知识：\n" + String.join("\n", contextDocs);
        }

        String fullMessage = userMessage + contextPrompt;

        String aiResponse = chatClient.prompt()
                .user(fullMessage)
                .call()
                .content();

        ChatMessage userMsg = new ChatMessage();
        userMsg.setSessionId(sessionId);
        userMsg.setRole("user");
        userMsg.setContent(userMessage);
        userMsg.setModel(session.getModel());
        userMsg.setCreatedAt(LocalDateTime.now());
        messageMapper.insert(userMsg);

        ChatMessage aiMsg = new ChatMessage();
        aiMsg.setSessionId(sessionId);
        aiMsg.setRole("assistant");
        aiMsg.setContent(aiResponse);
        aiMsg.setModel(session.getModel());
        aiMsg.setTokenCount(estimateToken(aiResponse));
        try {
            aiMsg.setContextDocs(objectMapper.writeValueAsString(contextDocs));
        } catch (JsonProcessingException e) {
            aiMsg.setContextDocs("[]");
        }
        aiMsg.setCreatedAt(LocalDateTime.now());
        messageMapper.insert(aiMsg);

        session.setUpdatedAt(LocalDateTime.now());
        sessionMapper.updateById(session);

        return aiMsg;
    }

    @Override
    public List<ChatMessage> getHistory(String sessionId) {
        ChatSession session = getSessionBySessionId(sessionId);
        if (session == null) {
            throw new BusinessException("会话不存在");
        }

        LambdaQueryWrapper<ChatMessage> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(ChatMessage::getSessionId, sessionId)
               .orderByAsc(ChatMessage::getCreatedAt);
        return messageMapper.selectList(wrapper);
    }

    @Override
    public List<ChatSession> getUserSessions(Long userId) {
        LambdaQueryWrapper<ChatSession> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(ChatSession::getUserId, userId)
               .orderByDesc(ChatSession::getUpdatedAt);
        return sessionMapper.selectList(wrapper);
    }

    private ChatSession getSessionBySessionId(String sessionId) {
        LambdaQueryWrapper<ChatSession> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(ChatSession::getSessionId, sessionId);
        return sessionMapper.selectOne(wrapper);
    }

    private int estimateToken(String text) {
        return text != null ? text.length() / 2 : 0;
    }
}