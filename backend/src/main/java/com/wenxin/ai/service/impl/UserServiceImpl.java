package com.wenxin.ai.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.wenxin.ai.common.dto.ResponseResult;
import com.wenxin.ai.common.enums.HttpCodeEnum;
import com.wenxin.ai.common.holder.RequestHolder;
import com.wenxin.ai.dto.EditPasswordDto;
import com.wenxin.ai.dto.LoginDto;
import com.wenxin.ai.dto.RegisterDto;
import com.wenxin.ai.entity.User;
import com.wenxin.ai.mapper.UserMapper;
import com.wenxin.ai.service.UserService;
import com.wenxin.ai.utils.JwtUtils;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.TimeoutUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.DigestUtils;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.TimeUnit;

@Slf4j
@Service
public class UserServiceImpl extends ServiceImpl<UserMapper, User> implements UserService {
    @Resource
    private RedisTemplate<String, String> redisTemplate;

    // 用户注册
    @Override
    @Transactional
    public ResponseResult register(RegisterDto dto) {
        // 1.判断两次密码是否一致
        if (!dto.getPassword().equals(dto.getAgainPwd())) {
            return ResponseResult.errorResult(HttpCodeEnum.PARAM_INVALID, "两次密码不一致");
        }
        // 2.获取缓存中的验证状态
        String verifyState = redisTemplate.opsForValue().get("verify:email:" + dto.getEmail());
        if (!"1".equals(verifyState)) {
            return ResponseResult.errorResult(HttpCodeEnum.PARAM_INVALID, "邮箱未验证");
        }
        // 3.邮箱已验证，查看该邮箱是否注册
        Optional<User> existingUser = Optional.ofNullable(getUserByEmail(dto.getEmail()));
        if (existingUser.isPresent()) {
            // 该邮箱已经注册
            return ResponseResult.errorResult(HttpCodeEnum.PARAM_INVALID, "该邮箱已被注册");
        }
        User user = new User();
        user.setEmail(dto.getEmail());
        user.setUsername(dto.getUsername());
        user.setCreateTime(LocalDateTime.now());
        String salt = DigestUtils.md5DigestAsHex(dto.getEmail().getBytes());
        user.setSalt(salt);
        user.setPassword(encryptPassword(dto.getPassword(), salt));
        // 保存用户
        save(user);
        // 删除缓存验证
        redisTemplate.delete("verify:email:" + dto.getEmail());
        return ResponseResult.okResult("注册成功");
    }

    @Override
    public ResponseResult login(LoginDto dto) {
        System.out.println(dto.toString());
        // 根据email查询用户
        Optional<User> existingUser = Optional.ofNullable(getUserByEmail(dto.getEmail()));
        if (existingUser.isEmpty()) {
            // 该邮箱未注册
            return ResponseResult.errorResult(HttpCodeEnum.PARAM_INVALID, "该邮箱还未注册");
        }
        // 判断密码是否与数据库密码一致
        String encryptPassword = encryptPassword(dto.getPassword(), existingUser.get().getSalt());
        if (!encryptPassword.equals(existingUser.get().getPassword())) {
            return ResponseResult.errorResult(HttpCodeEnum.LOGIN_PASSWORD_ERROR);
        }
        // 密码正确，生成 Token 返回数据
        String token = JwtUtils.createTokenByUserId(existingUser.get().getUserId());
        // 对 token 进行缓存
        redisTemplate.opsForValue().set("user:login:" + existingUser.get().getUserId(), token, 7, TimeUnit.DAYS);
        existingUser.get().setSalt("");
        existingUser.get().setPassword("");
        Map<String, Object> map = new HashMap<>();
        map.put("token", token);
        map.put("user", existingUser.get());
        return ResponseResult.okResult(map);
    }

    @Override
    public User getUserByEmail(String email) {
        LambdaQueryWrapper<User> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(User::getEmail, email);
        return getOne(queryWrapper);
    }



    // 作为用户去获取用户信息的方法，会处理密码等敏感信息
    @Override
    public ResponseResult getUserById(Long userId) {
        Optional<User> existingUser = Optional.ofNullable(getById(userId));
        if (existingUser.isPresent()) {
            // 处理敏感数据
            existingUser.get().setSalt("");
            existingUser.get().setPassword("");
            return ResponseResult.okResult(existingUser.get());
        }
        return ResponseResult.errorResult(HttpCodeEnum.DATA_NOT_EXIST);
    }

    @Override
    public ResponseResult logout() {
        Long userId = RequestHolder.getId();
        redisTemplate.delete("user:login:" + userId);
        return ResponseResult.okResult("退出登录成功");
    }

    // 修改密码
    @Override
    @Transactional
    public ResponseResult editPassword(EditPasswordDto dto) {
        // 根据ThreadLocal获取userId
        Long userId = RequestHolder.getId();
        Optional<User> existingUser = Optional.ofNullable(getById(userId));
        if (existingUser.isEmpty()) {
            return ResponseResult.errorResult(HttpCodeEnum.USER_DATA_NOT_EXIST);
        }
        // 判断是否经过邮箱验证 获取缓存中的验证状态
        String verifyState = redisTemplate.opsForValue().get("verify:email:" + existingUser.get().getEmail());
        log.info("验证邮箱状态：{}",verifyState);
        if (!"1".equals(verifyState)) {
            return ResponseResult.errorResult(HttpCodeEnum.PARAM_INVALID, "邮箱未验证");
        }
        // 邮箱验证成功
        // dto的原密码是否与数据库上的密码一致
        String salt = existingUser.get().getSalt();
        String encryptOldPassword = encryptPassword(dto.getOldPassword(), salt);
        if (!encryptOldPassword.equals(existingUser.get().getPassword())) {{
            return ResponseResult.errorResult(HttpCodeEnum.PARAM_INVALID, "原密码有误");
        }}
        // 原密码正确，修改密码
        String encryptNewPassword = encryptPassword(dto.getNewPassword(), salt);
        existingUser.get().setPassword(encryptNewPassword);
        boolean status = updateById(existingUser.get());
        if (status) {
            return ResponseResult.okResult("密码修改成功");
        } else {
            return ResponseResult.errorResult(HttpCodeEnum.ERROR, "密码修改失败");
        }
    }

    // 根据原始密码和盐进行加密
    private String encryptPassword(String password, String salt) {
        String saltedPassword = password + salt;
        return DigestUtils.md5DigestAsHex(saltedPassword.getBytes());
    }

}
