package com.example.foodproject.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.example.foodproject.entity.KnowledgeBase;
import com.example.foodproject.exception.BusinessException;
import com.example.foodproject.mapper.KnowledgeBaseMapper;
import com.example.foodproject.service.KnowledgeBaseService;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
public class KnowledgeBaseServiceImpl implements KnowledgeBaseService {

    private final KnowledgeBaseMapper knowledgeBaseMapper;

    public KnowledgeBaseServiceImpl(KnowledgeBaseMapper knowledgeBaseMapper) {
        this.knowledgeBaseMapper = knowledgeBaseMapper;
    }

    @Override
    public KnowledgeBase create(KnowledgeBase knowledgeBase) {
        knowledgeBase.setCreatedAt(LocalDateTime.now());
        knowledgeBase.setUpdatedAt(LocalDateTime.now());
        knowledgeBase.setDocumentCount(0);
        knowledgeBaseMapper.insert(knowledgeBase);
        return knowledgeBase;
    }

    @Override
    public KnowledgeBase update(KnowledgeBase knowledgeBase) {
        if (knowledgeBase.getId() == null) {
            throw new BusinessException("ID不能为空");
        }
        knowledgeBase.setUpdatedAt(LocalDateTime.now());
        knowledgeBaseMapper.updateById(knowledgeBase);
        return knowledgeBase;
    }

    @Override
    public void delete(Long id) {
        knowledgeBaseMapper.deleteById(id);
    }

    @Override
    public KnowledgeBase getById(Long id) {
        KnowledgeBase knowledgeBase = knowledgeBaseMapper.selectById(id);
        if (knowledgeBase == null) {
            throw new BusinessException("知识库不存在");
        }
        return knowledgeBase;
    }
}