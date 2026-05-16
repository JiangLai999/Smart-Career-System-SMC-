package com.example.smartcareer.config;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.crypto.SecretKey;
import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.Collections;

@Component
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private static final String SECRET_STRING = "smart-career-system-jwt-secret-key-2024-very-long-secure-key-for-hs256";
    private static final SecretKey SECRET_KEY = Keys.hmacShaKeyFor(SECRET_STRING.getBytes(StandardCharsets.UTF_8));

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {
        
        String authHeader = request.getHeader("Authorization");
        
        if (authHeader != null && authHeader.startsWith("Bearer ")) {
            String token = authHeader.substring(7);
            
            try {
                Claims claims = Jwts.parserBuilder()
                        .setSigningKey(SECRET_KEY)
                        .build()
                        .parseClaimsJws(token)
                        .getBody();
                
                String userType = claims.get("userType", String.class);
                String username = claims.getSubject();
                Long userId = claims.get("userId", Long.class);
                
                if (userType != null && userId != null) {
                    String role = getRoleFromUserType(userType);
                    
                    UsernamePasswordAuthenticationToken authentication = 
                            new UsernamePasswordAuthenticationToken(
                                    userId,
                                    null,
                                    Collections.singletonList(new SimpleGrantedAuthority(role))
                            );
                    
                    authentication.setDetails(username);
                    SecurityContextHolder.getContext().setAuthentication(authentication);
                }
            } catch (Exception e) {
                // Token invalid or expired, continue without authentication
            }
        }
        
        filterChain.doFilter(request, response);
    }
    
    private String getRoleFromUserType(String userType) {
        switch (userType) {
            case "SYSTEM_ADMIN":
                return "ROLE_ADMIN";
            case "ENTERPRISE_HR":
                return "ROLE_ENTERPRISE";
            case "JOB_SEEKER":
                return "ROLE_USER";
            default:
                return "ROLE_USER";
        }
    }
}
