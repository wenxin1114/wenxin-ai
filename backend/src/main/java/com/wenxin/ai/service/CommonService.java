package com.wenxin.ai.service;

import com.wenxin.ai.common.dto.ResponseResult;
import com.wenxin.ai.dto.SendEmailDto;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;

public interface CommonService {
    ResponseResult sendEmailVerify(SendEmailDto dto);

    ResponseResult emailVerify(String token);

    ResponseResult getPicCode(String timestamp) throws IOException;
}
