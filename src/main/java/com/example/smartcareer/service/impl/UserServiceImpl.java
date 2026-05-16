package com.example.smartcareer.service.impl;

import com.example.smartcareer.dto.request.LoginRequest;
import com.example.smartcareer.dto.request.RegisterRequest;
import com.example.smartcareer.dto.response.LoginResponse;
import com.example.smartcareer.entity.EnterpriseHR;
import com.example.smartcareer.entity.JobSeeker;
import com.example.smartcareer.entity.SystemAdmin;
import com.example.smartcareer.entity.User;
import com.example.smartcareer.exception.BusinessException;
import com.example.smartcareer.exception.ErrorCode;
import com.example.smartcareer.repository.UserRepository;
import com.example.smartcareer.service.UserService;
import com.example.smartcareer.util.JwtUtil;
import com.example.smartcareer.util.PasswordEncoder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
public class UserServiceImpl implements UserService {
    
    @Autowired
    private UserRepository userRepository;
    
    @Autowired
    private PasswordEncoder passwordEncoder;
    
    @Autowired
    private JwtUtil jwtUtil;
    
    @Override
    @Transactional
    public User register(RegisterRequest request) {
        if (existsByUsername(request.getUsername())) {
            throw new BusinessException(ErrorCode.USER_ALREADY_EXISTS);
        }
        
        if (request.getPhone() != null && existsByPhone(request.getPhone())) {
            throw new BusinessException(ErrorCode.PHONE_ALREADY_EXISTS);
        }
        
        if (request.getEmail() != null && existsByEmail(request.getEmail())) {
            throw new BusinessException(ErrorCode.EMAIL_ALREADY_EXISTS);
        }
        
        User user;
        String userType = request.getUserType();
        if ("JOB_SEEKER".equals(userType)) {
            user = new JobSeeker();
        } else if ("ENTERPRISE_HR".equals(userType)) {
            user = new EnterpriseHR();
        } else if ("SYSTEM_ADMIN".equals(userType)) {
            user = new SystemAdmin();
        } else {
            throw new BusinessException(ErrorCode.BAD_REQUEST, "不支持的用户类型: " + userType);
        }
        
        user.setUsername(request.getUsername());
        user.setPassword(encodePassword(request.getPassword()));
        user.setPhone(request.getPhone());
        user.setEmail(request.getEmail());
        user.setUserType(request.getUserType());
        user.setStatus(1);
        
        return userRepository.save(user);
    }
    
    @Override
    public LoginResponse login(LoginRequest request) {
        User user;
        
        // 支持用户名或手机号登录
        String loginName = request.getUsername();
        if (loginName.matches("^1[3-9]\\d{9}$")) {
            // 手机号格式，使用手机号登录
            user = userRepository.findByPhone(loginName).orElse(null);
            if (user == null) {
                throw new BusinessException(ErrorCode.USER_NOT_FOUND);
            }
        } else {
            // 用户名登录
            user = getUserByUsername(loginName);
        }
        
        if (user.getStatus() != 1) {
            throw new BusinessException(ErrorCode.ACCOUNT_DISABLED);
        }
        
        if (!checkPassword(request.getPassword(), user.getPassword())) {
            throw new BusinessException(ErrorCode.PASSWORD_ERROR);
        }
        
        String token = jwtUtil.generateToken(user.getId(), user.getUsername(), user.getUserType());
        
        LoginResponse response = new LoginResponse();
        response.setToken(token);
        response.setUserId(user.getId());
        response.setUsername(user.getUsername());
        response.setUserType(user.getUserType());
        
        return response;
    }
    
    @Override
    public User getUserById(Long id) {
        return userRepository.findById(id)
                .orElseThrow(() -> new BusinessException(ErrorCode.USER_NOT_FOUND));
    }
    
    @Override
    public User getUserByUsername(String username) {
        return userRepository.findByUsername(username)
                .orElseThrow(() -> new BusinessException(ErrorCode.USER_NOT_FOUND));
    }
    
    @Override
    public User getUserByPhone(String phone) {
        return userRepository.findByPhone(phone).orElse(null);
    }
    
    @Override
    @Transactional
    public User updateUser(Long id, User user) {
        User existingUser = getUserById(id);
        existingUser.setPhone(user.getPhone());
        existingUser.setEmail(user.getEmail());
        return userRepository.save(existingUser);
    }
    
    @Override
    @Transactional
    public void deleteUser(Long id) {
        User user = getUserById(id);
        user.setStatus(0);
        userRepository.save(user);
    }
    
    @Override
    public boolean existsByUsername(String username) {
        return userRepository.existsByUsername(username);
    }
    
    @Override
    public boolean existsByPhone(String phone) {
        return userRepository.existsByPhone(phone);
    }
    
    @Override
    public boolean existsByEmail(String email) {
        return userRepository.existsByEmail(email);
    }
    
    @Override
    public boolean checkPassword(String rawPassword, String encodedPassword) {
        return passwordEncoder.matches(rawPassword, encodedPassword);
    }
    
    @Override
    public String encodePassword(String password) {
        return passwordEncoder.encode(password);
    }
}
