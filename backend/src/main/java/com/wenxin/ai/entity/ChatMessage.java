package com.wenxin.ai.entity;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class ChatMessage {
    // 创建时间
    private LocalDateTime datetime;
    // 文本内容
    private String content;
    // user 是用户 assistant 是模型 system 是对话背景
    private String role;
}
