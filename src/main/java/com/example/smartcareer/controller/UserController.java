package com.example.smartcareer.controller;

import com.example.smartcareer.dto.request.LoginRequest;
import com.example.smartcareer.dto.request.RegisterRequest;
import com.example.smartcareer.dto.request.VerifyCodeRequest;
import com.example.smartcareer.dto.response.ApiResponse;
import com.example.smartcareer.dto.response.LoginResponse;
import com.example.smartcareer.dto.response.UserResponse;
import com.example.smartcareer.entity.User;
import com.example.smartcareer.service.UserService;
import com.example.smartcareer.service.VerificationCodeService;
import com.example.smartcareer.util.JwtUtil;
import com.example.smartcareer.util.PasswordEncoder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.Map;

@RestController
@RequestMapping("/api/user")
public class UserController {
    
    @Autowired
    private UserService userService;
    
    @Autowired
    private JwtUtil jwtUtil;
    
    @Autowired
    private VerificationCodeService verificationCodeService;
    
    @Autowired
    private PasswordEncoder passwordEncoder;
    
    @PostMapping("/register")
    public ApiResponse<UserResponse> register(@Valid @RequestBody RegisterRequest request) {
        User user = userService.register(request);
        
        UserResponse response = new UserResponse();
        response.setId(user.getId());
        response.setUsername(user.getUsername());
        response.setPhone(user.getPhone());
        response.setEmail(user.getEmail());
        response.setUserType(user.getUserType());
        response.setStatus(user.getStatus());
        response.setCreateTime(user.getCreateTime());
        
        return ApiResponse.success("注册成功", response);
    }
    
    @PostMapping("/login")
    public ApiResponse<LoginResponse> login(@Valid @RequestBody LoginRequest request) {
        LoginResponse response = userService.login(request);
        return ApiResponse.success("登录成功", response);
    }
    
    @GetMapping("/{id}")
    public ApiResponse<UserResponse> getUserById(@PathVariable Long id) {
        User user = userService.getUserById(id);
        
        UserResponse response = new UserResponse();
        response.setId(user.getId());
        response.setUsername(user.getUsername());
        response.setPhone(user.getPhone());
        response.setEmail(user.getEmail());
        response.setUserType(user.getUserType());
        response.setStatus(user.getStatus());
        response.setCreateTime(user.getCreateTime());
        
        return ApiResponse.success(response);
    }
    
    @GetMapping("/info")
    public ApiResponse<UserResponse> getCurrentUser(@RequestHeader("Authorization") String token) {
        String jwtToken = token.substring(7);
        Long userId = jwtUtil.extractUserId(jwtToken);
        
        User user = userService.getUserById(userId);
        
        UserResponse response = new UserResponse();
        response.setId(user.getId());
        response.setUsername(user.getUsername());
        response.setPhone(user.getPhone());
        response.setEmail(user.getEmail());
        response.setUserType(user.getUserType());
        response.setStatus(user.getStatus());
        response.setCreateTime(user.getCreateTime());
        
        return ApiResponse.success(response);
    }
    
    @PostMapping("/reset-password")
    public ApiResponse<String> resetPassword(@RequestBody Map<String, String> request) {
        try {
            String phone = request.get("phone");
            String code = request.get("code");
            String newPassword = request.get("newPassword");
            
            if (phone == null || code == null || newPassword == null) {
                return ApiResponse.error("参数不完整");
            }
            
            // 验证验证码
            boolean verified = verificationCodeService.verifyCode(phone, code, "RESET_PASSWORD");
            if (!verified) {
                return ApiResponse.error("验证码错误或已过期");
            }
            
            User user = userService.getUserByPhone(phone);
            if (user == null) {
                return ApiResponse.error("用户不存在");
            }
            
            user.setPassword(passwordEncoder.encode(newPassword));
            userService.updateUser(user.getId(), user);
            
            return ApiResponse.success("密码重置成功");
        } catch (Exception e) {
            return ApiResponse.error("重置密码失败: " + e.getMessage());
        }
    }
    
    @PostMapping("/change-password")
    public ApiResponse<String> changePassword(
            @RequestBody Map<String, String> request,
            @RequestHeader("Authorization") String token) {
        String jwtToken = token.substring(7);
        Long userId = jwtUtil.extractUserId(jwtToken);
        
        String oldPassword = request.get("oldPassword");
        String newPassword = request.get("newPassword");
        
        User user = userService.getUserById(userId);
        
        if (!passwordEncoder.matches(oldPassword, user.getPassword())) {
            return ApiResponse.error("原密码错误");
        }
        
        user.setPassword(passwordEncoder.encode(newPassword));
        userService.updateUser(userId, user);
        
        return ApiResponse.success("密码修改成功");
    }
}
