package com.wenxin.ai.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.wenxin.ai.common.dto.ResponseResult;
import com.wenxin.ai.dto.EditPasswordDto;
import com.wenxin.ai.dto.LoginDto;
import com.wenxin.ai.dto.RegisterDto;
import com.wenxin.ai.entity.User;

public interface UserService extends IService<User> {
    // 用户注册
    ResponseResult register(RegisterDto dto);
    // 用户登录
    ResponseResult login(LoginDto dto);

    // 根据 email 查询用户
    User getUserByEmail(String email);

    // 根据 userId 查询用户
    ResponseResult getUserById(Long userId);

    ResponseResult logout();

    ResponseResult editPassword(EditPasswordDto dto);
}
