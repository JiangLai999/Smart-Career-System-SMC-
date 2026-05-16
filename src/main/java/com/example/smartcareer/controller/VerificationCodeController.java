package com.example.smartcareer.controller;

import com.example.smartcareer.dto.request.VerifyCodeRequest;
import com.example.smartcareer.dto.request.VerificationCodeRequest;
import com.example.smartcareer.dto.response.ApiResponse;
import com.example.smartcareer.service.VerificationCodeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
@RequestMapping("/api/verification")
public class VerificationCodeController {
    
    @Autowired
    private VerificationCodeService verificationCodeService;
    
    @PostMapping("/send")
    public ApiResponse<String> sendVerificationCode(@Valid @RequestBody VerificationCodeRequest request) {
        try {
            verificationCodeService.sendVerificationCode(request.getPhone(), request.getPurpose());
            return ApiResponse.success("验证码发送成功");
        } catch (Exception e) {
            return ApiResponse.error("发送失败: " + e.getMessage());
        }
    }
    
    @PostMapping("/verify")
    public ApiResponse<Boolean> verifyCode(@Valid @RequestBody VerifyCodeRequest request) {
        try {
            boolean verified = verificationCodeService.verifyCode(
                    request.getPhone(),
                    request.getCode(),
                    request.getPurpose()
            );
            if (verified) {
                return ApiResponse.success("验证成功", true);
            } else {
                return ApiResponse.error("验证码错误或已过期");
            }
        } catch (Exception e) {
            return ApiResponse.error("验证失败: " + e.getMessage());
        }
    }
}
