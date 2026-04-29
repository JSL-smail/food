package com.example.foodproject.service;

import com.example.foodproject.dto.LoginRequest;
import com.example.foodproject.dto.RegisterRequest;

public interface AuthService {
    String login(LoginRequest request);
    void register(RegisterRequest request);
}
