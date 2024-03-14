package com.wenxin.ai.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class RegisterDto {
    @NotBlank(message = "用户昵称不得为空")
    @Size(min = 1, max = 8, message = "用户昵称长度不得于小于1，大于8")
    private String username;
    @Email(message = "邮箱不规范")
    private String email;
    @NotBlank(message = "密码不得为空")
    @Size(min = 6, max = 12, message = "密码长度不得小于6, 大于12")
    private String password;
    @NotBlank(message = "二次密码不得为空")
    @Size(min = 6, max = 12, message = "二次密码长度不得小于6, 大于12")
    private String againPwd;
}
