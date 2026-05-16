package com.example.smartcareer.service;

import com.example.smartcareer.dto.request.LoginRequest;
import com.example.smartcareer.dto.request.RegisterRequest;
import com.example.smartcareer.dto.response.LoginResponse;
import com.example.smartcareer.entity.User;

public interface UserService {
    
    User register(RegisterRequest request);
    
    LoginResponse login(LoginRequest request);
    
    User getUserById(Long id);
    
    User getUserByUsername(String username);
    
    User getUserByPhone(String phone);
    
    User updateUser(Long id, User user);
    
    void deleteUser(Long id);
    
    boolean existsByUsername(String username);
    
    boolean existsByPhone(String phone);
    
    boolean existsByEmail(String email);
    
    boolean checkPassword(String rawPassword, String encodedPassword);
    
    String encodePassword(String password);
}
