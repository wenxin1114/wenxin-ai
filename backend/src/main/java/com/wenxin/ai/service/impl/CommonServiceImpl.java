package com.wenxin.ai.service.impl;

import cn.hutool.captcha.CaptchaUtil;
import cn.hutool.captcha.LineCaptcha;
import com.wenxin.ai.common.dto.ResponseResult;
import com.wenxin.ai.common.enums.HttpCodeEnum;
import com.wenxin.ai.dto.SendEmailDto;
import com.wenxin.ai.service.CommonService;
import com.wenxin.ai.utils.JwtUtils;
import io.jsonwebtoken.Claims;
import io.micrometer.common.util.StringUtils;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

import java.io.ByteArrayOutputStream;
import java.util.Base64;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.TimeUnit;


@Service
@Slf4j
public class CommonServiceImpl implements CommonService {

    @Value("${base-url}")
    private String baseUrl;

    @Resource
    private JavaMailSender javaMailSender;

    @Resource
    private RedisTemplate<String, String> redisTemplate;

    @Override
    public ResponseResult sendEmailVerify(SendEmailDto dto) {
        // 图片验证码验证
        String realCode = redisTemplate.opsForValue().get("code:pic:" + dto.getTimestamp());
        if (!dto.getPicCode().equalsIgnoreCase(realCode)) {
            return ResponseResult.errorResult(HttpCodeEnum.PARAM_INVALID, "图片验证码错误");
        }
        // 验证成功，删除验证码缓存
        redisTemplate.delete("code:pic:" + dto.getTimestamp());
        String token = JwtUtils.createTokenByEmail(dto.getEmail());
        String verifyUrl = baseUrl + "/api/common/email/verify/" + token;
        SimpleMailMessage simpleMailMessage = new SimpleMailMessage();
        simpleMailMessage.setFrom("wenxin_web@163.com");
        simpleMailMessage.setTo(dto.getEmail());
        simpleMailMessage.setSubject("邮箱验证");
        simpleMailMessage.setText("【问心平台】: 您正在进行邮箱验证，请点击该链接进行验证：" + verifyUrl + "，请在5分钟内验证！");
        CompletableFuture.runAsync(() -> javaMailSender.send(simpleMailMessage));
        redisTemplate.opsForValue().set("verify:email:" + dto.getEmail(), "0", 5, TimeUnit.MINUTES);
        return ResponseResult.okResult("邮箱验证发送成功");
    }

    @Override
    public ResponseResult emailVerify(String token) {
        if (!JwtUtils.validateToken(token)) {
            // token 失效
            return ResponseResult.errorResult(HttpCodeEnum.TOKEN_EXPIRE);
        }
        Claims claims = JwtUtils.parseToken(token);
        String email = (String) claims.get("email");
        String state = redisTemplate.opsForValue().get("verify:email:" + email);
        if ("0".equals(state)) {
            // 有邮箱验证
            redisTemplate.opsForValue().set("verify:email:" + email, "1", 5, TimeUnit.MINUTES);
            return ResponseResult.okResult("验证成功，请在五分钟之内进行操作，否则验证将失效");
        } else if ("1".equals(state)) {
            // 已验证
            return ResponseResult.okResult("已验证，无需重复操作");
        } else {
            return ResponseResult.errorResult(HttpCodeEnum.TOKEN_INVALID);
        }
    }

    @Override
    public ResponseResult getPicCode(String timestamp) {
        if (StringUtils.isBlank(timestamp)) {
            System.out.println("没有时间戳");
            timestamp = String.valueOf(System.currentTimeMillis());
        }
        // 1.生成图片验证码
        LineCaptcha captcha = CaptchaUtil.createLineCaptcha(200, 100, 4, 50);
        String picCode = captcha.getCode();
        log.info("图片验证码生成：" + picCode);
        // 2.保存到缓存中
        redisTemplate.opsForValue().set("code:pic:" + timestamp, picCode, 5, TimeUnit.MINUTES);
//        response.setContentType("image/jpeg");
//        response.setHeader("Pragma", "No-cache");
        // 3.返回图片验证码数据流
//        captcha.write(response.getOutputStream());
        // 将验证码图片转换为Base64格式
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        captcha.write(outputStream);
        String base64Image = Base64.getEncoder().encodeToString(outputStream.toByteArray());
        return ResponseResult.okResult(base64Image);
    }
}
