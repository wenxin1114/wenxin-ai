package com.wenxin.ai.dto;


import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class EditPasswordDto {
    @NotBlank(message = "原密码不得为空")
    @Size(min = 6, max = 12, message = "原密码长度不得小于6, 大于12")
    private String oldPassword;
    @NotBlank(message = "新密码长度不得为空")
    @Size(min = 6, max = 12, message = "新密码长度不得小于6, 大于12")
    private String newPassword;
}
