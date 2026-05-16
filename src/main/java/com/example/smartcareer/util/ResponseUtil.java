package com.example.smartcareer.util;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 响应工具类
 * 用于统一API响应格式
 */
public class ResponseUtil {
    
    /**
     * 成功响应
     */
    public static Map<String, Object> success() {
        Map<String, Object> result = new HashMap<>();
        result.put("code", 200);
        result.put("message", "操作成功");
        result.put("success", true);
        return result;
    }
    
    /**
     * 成功响应（带数据）
     */
    public static Map<String, Object> success(Object data) {
        Map<String, Object> result = success();
        result.put("data", data);
        return result;
    }
    
    /**
     * 成功响应（带消息）
     */
    public static Map<String, Object> success(String message) {
        Map<String, Object> result = success();
        result.put("message", message);
        return result;
    }
    
    /**
     * 成功响应（带消息和数据）
     */
    public static Map<String, Object> success(String message, Object data) {
        Map<String, Object> result = success();
        result.put("message", message);
        result.put("data", data);
        return result;
    }
    
    /**
     * 失败响应
     */
    public static Map<String, Object> error() {
        Map<String, Object> result = new HashMap<>();
        result.put("code", 500);
        result.put("message", "操作失败");
        result.put("success", false);
        return result;
    }
    
    /**
     * 失败响应（带消息）
     */
    public static Map<String, Object> error(String message) {
        Map<String, Object> result = error();
        result.put("message", message);
        return result;
    }
    
    /**
     * 失败响应（带状态码和消息）
     */
    public static Map<String, Object> error(int code, String message) {
        Map<String, Object> result = error();
        result.put("code", code);
        result.put("message", message);
        return result;
    }
    
    /**
     * 分页响应
     */
    public static Map<String, Object> page(List<?> list, long total, int page, int size) {
        Map<String, Object> result = success();
        
        Map<String, Object> data = new HashMap<>();
        data.put("list", list);
        data.put("total", total);
        data.put("page", page);
        data.put("size", size);
        data.put("totalPages", (total + size - 1) / size);
        
        result.put("data", data);
        return result;
    }
    
    /**
     * 分页响应（使用Page对象）
     */
    public static Map<String, Object> page(Page<?> page) {
        Map<String, Object> result = success();
        
        Map<String, Object> data = new HashMap<>();
        data.put("list", page.getContent());
        data.put("total", page.getTotalElements());
        data.put("page", page.getNumber());
        data.put("size", page.getSize());
        data.put("totalPages", page.getTotalPages());
        data.put("first", page.isFirst());
        data.put("last", page.isLast());
        
        result.put("data", data);
        return result;
    }
    
    /**
     * 参数错误响应
     */
    public static Map<String, Object> badRequest(String message) {
        return error(400, message);
    }
    
    /**
     * 未授权响应
     */
    public static Map<String, Object> unauthorized() {
        return error(401, "未授权，请先登录");
    }
    
    /**
     * 禁止访问响应
     */
    public static Map<String, Object> forbidden() {
        return error(403, "权限不足，禁止访问");
    }
    
    /**
     * 资源不存在响应
     */
    public static Map<String, Object> notFound(String message) {
        return error(404, message);
    }
    
    /**
     * 服务器错误响应
     */
    public static Map<String, Object> serverError() {
        return error(500, "服务器内部错误");
    }
    
    /**
     * 服务器错误响应（带消息）
     */
    public static Map<String, Object> serverError(String message) {
        return error(500, message);
    }
}
