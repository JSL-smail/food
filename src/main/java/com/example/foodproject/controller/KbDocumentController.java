package com.example.foodproject.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.example.foodproject.common.Result;
import com.example.foodproject.entity.KbDocument;
import com.example.foodproject.entity.KbDocumentChunk;
import com.example.foodproject.mapper.KbDocumentChunkMapper;
import com.example.foodproject.service.KbDocumentService;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

@RestController
@RequestMapping("/api/kb/document")
public class KbDocumentController {

    private final KbDocumentService kbDocumentService;
    private final KbDocumentChunkMapper chunkMapper;

    public KbDocumentController(KbDocumentService kbDocumentService, KbDocumentChunkMapper chunkMapper) {
        this.kbDocumentService = kbDocumentService;
        this.chunkMapper = chunkMapper;
    }

    @PostMapping("/upload")
    @PreAuthorize("hasAuthority('kb:document:upload')")
    public Result<Map<String, Object>> upload(
            @RequestParam("file") MultipartFile file,
            @RequestParam("kbId") Long kbId,
            @RequestParam(value = "title", required = false) String title) {
        try {
            String uploadDir = System.getProperty("java.io.tmpdir") + "/kb-docs/";
            File dir = new File(uploadDir);
            if (!dir.exists()) {
                dir.mkdirs();
            }

            String originalFilename = file.getOriginalFilename();
            String fileType = originalFilename != null && originalFilename.contains(".")
                    ? originalFilename.substring(originalFilename.lastIndexOf(".") + 1)
                    : "txt";

            String savedFileName = UUID.randomUUID().toString() + "." + fileType;
            File savedFile = new File(uploadDir + savedFileName);
            file.transferTo(savedFile);

            KbDocument document = new KbDocument();
            document.setTitle(title != null ? title : originalFilename);
            document.setKbId(kbId);
            document.setFilePath(savedFile.getAbsolutePath());
            document.setFileType(fileType);
            document.setCreatedAt(LocalDateTime.now());
            document.setUpdatedAt(LocalDateTime.now());

            KbDocument saved = kbDocumentService.uploadDocument(document, savedFile.getAbsolutePath());

            Map<String, Object> result = new HashMap<>();
            result.put("id", saved.getId());
            result.put("title", saved.getTitle());
            result.put("chunkCount", saved.getChunkCount());
            result.put("status", saved.getStatus());

            return Result.success(result);
        } catch (Exception e) {
            return Result.error("上传失败: " + e.getMessage());
        }
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasAuthority('kb:document:get')")
    public Result<KbDocument> getById(@PathVariable Long id) {
        return Result.success(kbDocumentService.getById(id));
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasAuthority('kb:document:delete')")
    public Result<String> delete(@PathVariable Long id) {
        kbDocumentService.delete(id);
        return Result.success("删除成功");
    }

    @GetMapping("/{id}/chunks")
    @PreAuthorize("hasAuthority('kb:document:get')")
    public Result<List<KbDocumentChunk>> getChunks(@PathVariable Long id) {
        LambdaQueryWrapper<KbDocumentChunk> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(KbDocumentChunk::getDocumentId, id)
               .orderByAsc(KbDocumentChunk::getChunkIndex);
        return Result.success(chunkMapper.selectList(wrapper));
    }

    @PostMapping("/{id}/process")
    @PreAuthorize("hasAuthority('kb:document:process')")
    public Result<String> processDocument(@PathVariable Long id) {
        kbDocumentService.processDocument(id);
        return Result.success("处理完成");
    }

    @GetMapping("/page")
    @PreAuthorize("hasAuthority('kb:document:list')")
    public Result<IPage<KbDocument>> page(
            @RequestParam(defaultValue = "1") Integer current,
            @RequestParam(defaultValue = "10") Integer size,
            @RequestParam(required = false) Long kbId) {
        Page<KbDocument> page = new Page<>(current, size);
        LambdaQueryWrapper<KbDocument> wrapper = new LambdaQueryWrapper<>();
        if (kbId != null) {
            wrapper.eq(KbDocument::getKbId, kbId);
        }
        wrapper.orderByDesc(KbDocument::getCreatedAt);
        IPage<KbDocument> result = kbDocumentService.page(page, wrapper);
        return Result.success(result);
    }
}