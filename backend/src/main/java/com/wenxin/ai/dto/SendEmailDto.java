package com.wenxin.ai.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class SendEmailDto {
    @NotBlank(message = "图片验证码不得为空")
    private String picCode;
    @Email(message = "邮箱格式不正确")
    private String email;
    @NotBlank(message = "时间戳不得为空")
    private String timestamp;
}
