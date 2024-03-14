package com.wenxin.ai.controller;


import com.wenxin.ai.common.dto.ResponseResult;
import com.wenxin.ai.dto.SendEmailDto;
import com.wenxin.ai.service.CommonService;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import java.io.IOException;



@Controller
@Slf4j
@RequestMapping("/common")
public class CommonController {
    @Resource
    private CommonService commonService;


    // 发送邮箱验证
    @PostMapping("/email/send")
    @ResponseBody
    public ResponseResult sendEmailVerify(@Validated @RequestBody SendEmailDto dto) {
        return commonService.sendEmailVerify(dto);
    }


    /*邮箱验证*/
    @GetMapping("/email/verify/{token}")
    public String emailVerify(@PathVariable("token") String token, Model model) {
        ResponseResult result =  commonService.emailVerify(token);
        model.addAttribute("result", result);
        return "emailVerifyResult";
    }

    // 图片验证码
    @GetMapping("/code/pic")
    @ResponseBody
    public ResponseResult getPicCode(String timestamp) throws IOException {
        return commonService.getPicCode(timestamp);
    }

}
