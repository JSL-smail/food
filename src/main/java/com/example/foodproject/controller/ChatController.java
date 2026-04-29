package com.example.foodproject.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.example.foodproject.common.Result;
import com.example.foodproject.entity.ChatMessage;
import com.example.foodproject.entity.ChatSession;
import com.example.foodproject.security.SecurityUtils;
import com.example.foodproject.service.ChatService;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/chat")
public class ChatController {

    private final ChatService chatService;

    public ChatController(ChatService chatService) {
        this.chatService = chatService;
    }

    @PostMapping("/session")
    @PreAuthorize("hasAuthority('chat:session:create')")
    public Result<ChatSession> createSession(
            @RequestParam(required = false) String model,
            @RequestParam(required = false) Long knowledgeBaseId) {
        Long userId = SecurityUtils.getUserId();
        if (userId == null) {
            return Result.error(401, "请先登录");
        }
        return Result.success(chatService.createSession(userId, model, knowledgeBaseId));
    }

    @PostMapping("/message")
    @PreAuthorize("hasAuthority('chat:message:send')")
    public Result<ChatMessage> sendMessage(
            @RequestParam String sessionId,
            @RequestParam String message,
            @RequestParam(required = false) Long knowledgeBaseId) {
        return Result.success(chatService.sendMessage(sessionId, message, knowledgeBaseId));
    }

    @GetMapping("/history/{sessionId}")
    @PreAuthorize("hasAuthority('chat:history:get')")
    public Result<List<ChatMessage>> getHistory(@PathVariable String sessionId) {
        return Result.success(chatService.getHistory(sessionId));
    }

    @GetMapping("/sessions")
    @PreAuthorize("hasAuthority('chat:session:list')")
    public Result<List<ChatSession>> getUserSessions() {
        Long userId = SecurityUtils.getUserId();
        if (userId == null) {
            return Result.error(401, "请先登录");
        }
        return Result.success(chatService.getUserSessions(userId));
    }

    @PostMapping("/stream")
    @PreAuthorize("hasAuthority('chat:message:send')")
    public Result<Map<String, String>> sendMessageStream(
            @RequestParam String sessionId,
            @RequestParam String message,
            @RequestParam(required = false) Long knowledgeBaseId) {
        ChatMessage response = chatService.sendMessage(sessionId, message, knowledgeBaseId);

        Map<String, String> result = new HashMap<>();
        result.put("sessionId", sessionId);
        result.put("content", response.getContent());
        result.put("model", response.getModel());

        return Result.success(result);
    }
}