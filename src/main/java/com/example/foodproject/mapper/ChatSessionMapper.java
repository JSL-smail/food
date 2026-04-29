package com.example.foodproject.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.example.foodproject.entity.ChatSession;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface ChatSessionMapper extends BaseMapper<ChatSession> {
}