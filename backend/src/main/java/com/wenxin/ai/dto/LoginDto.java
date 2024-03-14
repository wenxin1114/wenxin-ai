package com.wenxin.ai.dto;


import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class LoginDto {
    @Email(message = "邮箱不规范")
    @NotBlank(message = "邮箱不得为空")
    private String email;
    @NotBlank(message = "密码不得为空")
    @Size(min = 6, max = 8, message = "密码长度不得小于6, 大于12")
    private String password;
}
