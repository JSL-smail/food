package com.example.foodproject.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.example.foodproject.common.Result;
import com.example.foodproject.entity.KnowledgeBase;
import com.example.foodproject.service.KnowledgeBaseService;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/kb")
public class KnowledgeBaseController {

    private final KnowledgeBaseService knowledgeBaseService;

    public KnowledgeBaseController(KnowledgeBaseService knowledgeBaseService) {
        this.knowledgeBaseService = knowledgeBaseService;
    }

    @PostMapping
    @PreAuthorize("hasAuthority('kb:add')")
    public Result<KnowledgeBase> create(@RequestBody KnowledgeBase knowledgeBase) {
        return Result.success(knowledgeBaseService.create(knowledgeBase));
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasAuthority('kb:update')")
    public Result<KnowledgeBase> update(@PathVariable Long id, @RequestBody KnowledgeBase knowledgeBase) {
        knowledgeBase.setId(id);
        return Result.success(knowledgeBaseService.update(knowledgeBase));
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasAuthority('kb:delete')")
    public Result<String> delete(@PathVariable Long id) {
        knowledgeBaseService.delete(id);
        return Result.success("删除成功");
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasAuthority('kb:get')")
    public Result<KnowledgeBase> getById(@PathVariable Long id) {
        return Result.success(knowledgeBaseService.getById(id));
    }

    @GetMapping("/page")
    @PreAuthorize("hasAuthority('kb:list')")
    public Result<IPage<KnowledgeBase>> page(
            @RequestParam(defaultValue = "1") Integer current,
            @RequestParam(defaultValue = "10") Integer size,
            @RequestParam(required = false) String name) {
        Page<KnowledgeBase> page = new Page<>(current, size);
        LambdaQueryWrapper<KnowledgeBase> wrapper = new LambdaQueryWrapper<>();
        if (name != null && !name.isEmpty()) {
            wrapper.like(KnowledgeBase::getName, name);
        }
        IPage<KnowledgeBase> result = knowledgeBaseService.page(page, wrapper);
        return Result.success(result);
    }
}