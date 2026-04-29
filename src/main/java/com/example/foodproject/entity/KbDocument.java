package com.example.foodproject.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import java.time.LocalDateTime;

@Data
@TableName("kb_document")
public class KbDocument {
    @TableId(type = IdType.AUTO)
    private Long id;
    private String title;
    private String content;
    private String filePath;
    private String fileType;
    private Long kbId;
    private Integer chunkCount;
    private String status;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}