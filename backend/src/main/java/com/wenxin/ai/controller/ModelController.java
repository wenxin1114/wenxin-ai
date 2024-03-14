package com.wenxin.ai.controller;


import com.wenxin.ai.ai.SparkModel;
import com.wenxin.ai.common.dto.ResponseResult;
import com.wenxin.ai.dto.ChatDto;
import jakarta.annotation.Resource;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/model")
public class ModelController {

    @Resource
    private SparkModel sparkModel;

    @PostMapping("/chat")
    public ResponseResult sendMessage(@Validated @RequestBody ChatDto dto) {
        return sparkModel.sendMessage(dto);
    }
}
