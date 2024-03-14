package com.wenxin.ai.dto;


import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class ChatDto {
    @NotBlank(message = "问题内容不能为空")
    private String question;
    @NotBlank(message = "chatId 不能为空")
    private String id;
}
