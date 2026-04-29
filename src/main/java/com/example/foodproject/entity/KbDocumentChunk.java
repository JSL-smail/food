package com.example.foodproject.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import java.time.LocalDateTime;

@Data
@TableName("kb_document_chunk")
public class KbDocumentChunk {
    @TableId(type = IdType.AUTO)
    private Long id;
    private Long documentId;
    private String content;
    private Integer chunkIndex;
    private String metadata;
    private LocalDateTime createdAt;
}