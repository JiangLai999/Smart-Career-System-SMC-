package com.example.smartcareer.service.impl;

import com.example.smartcareer.entity.VerificationCode;
import com.example.smartcareer.repository.VerificationCodeRepository;
import com.example.smartcareer.service.VerificationCodeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.Optional;
import java.util.Random;

@Service
public class VerificationCodeServiceImpl implements VerificationCodeService {
    
    @Autowired
    private VerificationCodeRepository verificationCodeRepository;
    
    private static final int CODE_LENGTH = 6;
    private static final int EXPIRE_MINUTES = 5;
    
    @Override
    @org.springframework.transaction.annotation.Transactional
    public void sendVerificationCode(String phone, String purpose) {
        verificationCodeRepository.invalidateCodes(phone, purpose);
        
        String code = generateCode();
        
        VerificationCode verificationCode = new VerificationCode();
        verificationCode.setPhone(phone);
        verificationCode.setCode(code);
        verificationCode.setPurpose(purpose);
        verificationCode.setExpireTime(LocalDateTime.now().plusMinutes(EXPIRE_MINUTES));
        verificationCode.setUsed(0);
        
        verificationCodeRepository.save(verificationCode);
        
        System.out.println("验证码已发送到手机 " + phone + "，验证码：" + code + "，用途：" + purpose);
    }
    
@Override
    @org.springframework.transaction.annotation.Transactional
    public boolean verifyCode(String phone, String code, String purpose) {
        System.out.println("开始验证验证码 - phone: " + phone + ", purpose: " + purpose + ", code: " + code);

        try {
            Optional<VerificationCode> validCodeOpt = verificationCodeRepository.findValidCode(phone, purpose, LocalDateTime.now());

            if (!validCodeOpt.isPresent()) {
                System.out.println("验证码验证失败：未找到有效验证码");
                return false;
            }

            VerificationCode verificationCode = validCodeOpt.get();
            System.out.println("找到验证码: " + verificationCode.getCode() + ", 过期时间: " + verificationCode.getExpireTime());
            
            if (!verificationCode.getCode().equals(code)) {
                System.out.println("验证码验证失败：验证码不匹配，输入: " + code + ", 实际: " + verificationCode.getCode());
                return false;
            }
            
            verificationCode.setUsed(1);
            verificationCodeRepository.save(verificationCode);
            
            System.out.println("验证码验证成功");
            return true;
        } catch (Exception e) {
            System.err.println("验证码验证异常: " + e.getMessage());
            e.printStackTrace();
            return false;
        }
    }
    
    @Override
    @Transactional
    public void invalidateCodes(String phone, String purpose) {
        verificationCodeRepository.invalidateCodes(phone, purpose);
    }
    
    private String generateCode() {
        Random random = new Random();
        StringBuilder code = new StringBuilder();
        for (int i = 0; i < CODE_LENGTH; i++) {
            code.append(random.nextInt(10));
        }
        return code.toString();
    }
}
