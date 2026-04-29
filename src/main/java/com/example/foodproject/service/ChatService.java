package com.example.foodproject.service;

import com.example.foodproject.entity.ChatSession;
import com.example.foodproject.entity.ChatMessage;
import java.util.List;

public interface ChatService {

    ChatSession createSession(Long userId, String model, Long knowledgeBaseId);

    ChatMessage sendMessage(String sessionId, String userMessage, Long knowledgeBaseId);

    List<ChatMessage> getHistory(String sessionId);

    List<ChatSession> getUserSessions(Long userId);
}