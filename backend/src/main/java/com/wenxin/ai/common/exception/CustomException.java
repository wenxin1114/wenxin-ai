package com.wenxin.ai.common.exception;

import com.wenxin.ai.common.enums.HttpCodeEnum;

public class CustomException extends RuntimeException {

    private HttpCodeEnum httpCodeEnum;

    public CustomException(HttpCodeEnum httpCodeEnum){
        this.httpCodeEnum = httpCodeEnum;
    }

    public HttpCodeEnum getHttpCodeEnum() {
        return httpCodeEnum;
    }
}
