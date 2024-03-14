package com.wenxin.ai.common.exception;


import com.wenxin.ai.common.dto.ResponseResult;
import com.wenxin.ai.common.enums.HttpCodeEnum;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;

@ControllerAdvice  //控制器增强类
@ResponseBody
@Slf4j
public class GlobalExceptionHandler {

    @ExceptionHandler(Exception.class)
    public ResponseResult exception(Exception e){
        log.error("catch exception:{}",e.getMessage());
        return ResponseResult.errorResult(HttpCodeEnum.SERVER_ERROR);
    }

    /**
     * 处理可控异常  自定义异常
     */
    @ExceptionHandler(CustomException.class)
    public ResponseResult exception(CustomException e){
        log.error("catch exception: {}", e.getLocalizedMessage());
        return ResponseResult.errorResult(e.getHttpCodeEnum());
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseResult handleValidationException(MethodArgumentNotValidException e) {
        BindingResult bindingResult = e.getBindingResult();
        FieldError fieldError = bindingResult.getFieldError();
        if (fieldError != null) {
            return ResponseResult.errorResult(HttpCodeEnum.PARAM_INVALID, fieldError.getDefaultMessage());
        }
        return ResponseResult.errorResult(HttpCodeEnum.SERVER_ERROR);
    }

}
