package com.wenxin.ai.controller;


import com.wenxin.ai.common.dto.ResponseResult;
import com.wenxin.ai.common.holder.RequestHolder;
import com.wenxin.ai.dto.EditPasswordDto;
import com.wenxin.ai.dto.LoginDto;
import com.wenxin.ai.dto.RegisterDto;
import com.wenxin.ai.service.UserService;
import jakarta.annotation.Resource;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/user")
public class UserController {

    @Resource
    private UserService userService;

    /* 用户注册 */
    @PostMapping("/register")
    public ResponseResult register(@Validated @RequestBody RegisterDto dto) {
        // 注册用户
        return userService.register(dto);
    }

    /* 用户登录 */
    @PostMapping("/login")
    public ResponseResult login(@Validated @RequestBody LoginDto dto) {
        // 注册用户
        return userService.login(dto);
    }

    // 获取用户信息
    @GetMapping("/info")
    public ResponseResult getUserInfo() {
        return userService.getUserById(RequestHolder.getId());
    }

    // 修改密码
    @PostMapping("/pwd/edit")
    public ResponseResult editPassword(@Validated @RequestBody EditPasswordDto dto) {
        return userService.editPassword(dto);
    }

    // 退出登录
    @GetMapping("/logout")
    public ResponseResult logout() {
        return userService.logout();
    }


}
