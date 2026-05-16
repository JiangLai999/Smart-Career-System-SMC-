package com.example.smartcareer.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.MediaType;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    @Autowired
    private JwtAuthenticationFilter jwtAuthenticationFilter;

    @Bean
    public PasswordEncoder securityPasswordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
            .cors().and()
            .csrf().disable()
            .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS)
            .and()
            .exceptionHandling()
                .authenticationEntryPoint((request, response, authException) -> {
                    response.setContentType(MediaType.APPLICATION_JSON_VALUE);
                    response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
                    Map<String, Object> body = new HashMap<>();
                    body.put("code", 401);
                    body.put("message", "未授权，请先登录");
                    body.put("data", null);
                    new ObjectMapper().writeValue(response.getOutputStream(), body);
                })
                .accessDeniedHandler((request, response, accessDeniedException) -> {
                    response.setContentType(MediaType.APPLICATION_JSON_VALUE);
                    response.setStatus(HttpServletResponse.SC_FORBIDDEN);
                    Map<String, Object> body = new HashMap<>();
                    body.put("code", 403);
                    body.put("message", "权限不足");
                    body.put("data", null);
                    new ObjectMapper().writeValue(response.getOutputStream(), body);
                })
            .and()
            .authorizeRequests()
            // 公开接口
            .antMatchers("/api/user/login", "/api/user/register").permitAll()
            .antMatchers("/api/company/login", "/api/company/register").permitAll()
            .antMatchers("/api/admin/login").permitAll()
            .antMatchers("/api/init/**").permitAll()
            .antMatchers("/api/verification/**").permitAll()
            .antMatchers("/api/user/reset-password").permitAll()
            .antMatchers("/api/company/reset-password").permitAll()
            .antMatchers("/api/company/notification/**").permitAll()
            // 公开职位查询
            .antMatchers("/api/job/list", "/api/job/search", "/api/job/{id}", "/api/job/filter").permitAll()
            .antMatchers("/api/job/enterprise/**").permitAll()
            // 静态资源和文档
            .antMatchers("/static/**", "/uploads/**").permitAll()
            .antMatchers("/swagger-ui.html", "/swagger-ui/**", "/v3/api-docs/**", "/doc.html", "/webjars/**").permitAll()
            // 管理端接口
            .antMatchers("/api/admin/**").hasAuthority("ROLE_ADMIN")
            // 企业端接口
            .antMatchers("/api/company/**").hasAuthority("ROLE_ENTERPRISE")
            .antMatchers("/api/interview/enterprise/**").hasAuthority("ROLE_ENTERPRISE")
            .antMatchers("/api/interview/enterprise").hasAuthority("ROLE_ENTERPRISE")
            .antMatchers("/api/interview/create").hasAuthority("ROLE_ENTERPRISE")
            .antMatchers("/api/interview/{id}/feedback").hasAuthority("ROLE_ENTERPRISE")
            .antMatchers("/api/interview/statistics").hasAuthority("ROLE_ENTERPRISE")
            .antMatchers("/api/interview/upcoming").hasAuthority("ROLE_ENTERPRISE")
            // 用户端接口
            .antMatchers("/api/user/**").hasAuthority("ROLE_USER")
            .antMatchers("/api/jobseeker/**").hasAuthority("ROLE_USER")
            .antMatchers("/api/application/**").hasAuthority("ROLE_USER")
            .antMatchers("/api/recommendation/**").hasAuthority("ROLE_USER")
            .antMatchers("/api/assessment/**").hasAuthority("ROLE_USER")
            .antMatchers("/api/interview/jobseeker/**").hasAuthority("ROLE_USER")
            .antMatchers("/api/interview/{id}/confirm").hasAuthority("ROLE_USER")
            .antMatchers("/api/interview/{id}/reject").hasAuthority("ROLE_USER")
            .antMatchers("/api/interview/{id}/cancel").hasAuthority("ROLE_USER")
            // 通知接口 - 用户和企业都可访问
            .antMatchers("/api/notification/**").hasAnyAuthority("ROLE_USER", "ROLE_ENTERPRISE")
            // 其他请求需要认证
            .anyRequest().authenticated()
            .and()
            .addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }
}
