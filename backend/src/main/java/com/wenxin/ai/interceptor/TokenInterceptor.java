package com.wenxin.ai.interceptor;

import com.alibaba.fastjson2.JSON;
import com.wenxin.ai.common.dto.ResponseResult;
import com.wenxin.ai.common.enums.HttpCodeEnum;
import com.wenxin.ai.common.holder.RequestHolder;
import com.wenxin.ai.utils.JwtUtils;
import io.jsonwebtoken.Claims;
import io.micrometer.common.util.StringUtils;
import jakarta.annotation.Resource;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import java.io.IOException;
import java.io.PrintWriter;

@Slf4j
@Component
public class TokenInterceptor implements HandlerInterceptor {
    @Resource
    public RedisTemplate<String, String> redisTemplate;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        log.info("Token拦截URI:{}", request.getRequestURI());
        // 获取 token
        String token = request.getHeader("token");
        // 判断是否有token
        if (StringUtils.isBlank(token)) {
            writeResponse(ResponseResult.errorResult(HttpCodeEnum.TOKEN_REQUIRE), response);
            return false;
        }
        if (!JwtUtils.validateToken(token)) {
            // token过期
            writeResponse(ResponseResult.errorResult(HttpCodeEnum.TOKEN_EXPIRE), response);
            return false;
        }
        // 解析token并验证
        Claims claims = JwtUtils.parseToken(token);
        if (claims == null || !JwtUtils.validateToken(token)) {
            writeResponse(ResponseResult.errorResult(HttpCodeEnum.TOKEN_INVALID), response);
            return false;
        }
        // 查询缓存 判断 token 是否过期
        Long userId = claims.get("userId", Long.class);
        String redisToken = redisTemplate.opsForValue().get("user:login:" + userId);

        if (!token.equals(redisToken)) {
            writeResponse(ResponseResult.errorResult(HttpCodeEnum.TOKEN_EXPIRE), response);
            return false;
        }
        // 无误，将 token 里的 userId 存入 ThreadLocal
        RequestHolder.add(userId);
        return true;
    }

    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, ModelAndView modelAndView) {
        // 请求处理之后进行拦截处理，比如记录日志或者修改响应
    }


    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) throws Exception {
        log.info("ThreadId:" + RequestHolder.getId());
        RequestHolder.remove();
        log.info("afterCompletion");
        return;
    }

    public void writeResponse(ResponseResult responseResult, HttpServletResponse response) throws IOException {
        response.setCharacterEncoding("UTF-8");
        response.setContentType("application/json;charset=UTF-8");
        PrintWriter writer = response.getWriter();
        writer.write(JSON.toJSONString(responseResult));
        writer.flush();
    }


}
