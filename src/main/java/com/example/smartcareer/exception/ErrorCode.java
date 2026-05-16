package com.example.smartcareer.exception;

public enum ErrorCode {
    
    SUCCESS(200, "操作成功"),
    BAD_REQUEST(400, "请求参数错误"),
    UNAUTHORIZED(401, "未授权"),
    FORBIDDEN(403, "禁止访问"),
    NOT_FOUND(404, "资源不存在"),
    METHOD_NOT_ALLOWED(405, "请求方法不允许"),
    INTERNAL_SERVER_ERROR(500, "服务器内部错误"),
    
    USER_NOT_FOUND(1001, "用户不存在"),
    USER_ALREADY_EXISTS(1002, "用户已存在"),
    PASSWORD_ERROR(1003, "密码错误"),
    ACCOUNT_DISABLED(1004, "账号已被禁用"),
    PHONE_ALREADY_EXISTS(1005, "手机号已被注册"),
    EMAIL_ALREADY_EXISTS(1006, "邮箱已被注册"),
    ACCOUNT_PENDING(1007, "账号审核中"),
    ACCOUNT_REJECTED(1008, "账号审核未通过"),
    
    JOB_NOT_FOUND(2001, "职位不存在"),
    JOB_EXPIRED(2002, "职位已过期"),
    JOB_NOT_ACTIVE(2003, "职位未激活"),
    
    APPLICATION_NOT_FOUND(3001, "申请不存在"),
    APPLICATION_ALREADY_EXISTS(3002, "已申请过该职位"),
    APPLICATION_STATUS_ERROR(3003, "申请状态错误"),
    
    ENTERPRISE_NOT_FOUND(4001, "企业不存在"),
    ENTERPRISE_NOT_AUDITED(4002, "企业未审核通过"),
    ENTERPRISE_ALREADY_EXISTS(4003, "企业已存在"),
    LICENSE_ALREADY_EXISTS(4004, "营业执照号已存在"),
    ENTERPRISE_NOT_APPROVED(4005, "企业审核未通过"),
    
    DUPLICATE_LICENSE(4006, "营业执照已被注册"),
    DUPLICATE_USERNAME(4007, "用户名已存在"),
    INVALID_PASSWORD(4008, "密码错误"),
    DUPLICATE_PHONE(4009, "手机号已被注册"),
    DUPLICATE_EMAIL(4010, "邮箱已被注册"),

    INTERVIEW_NOT_FOUND(5001, "面试不存在"),
    INTERVIEW_TIME_CONFLICT(5002, "面试时间冲突"),

    FILE_UPLOAD_ERROR(6001, "文件上传失败"),
    FILE_TYPE_ERROR(6002, "文件类型不支持"),

    PERMISSION_DENIED(7001, "权限不足"),
    
    RESOURCE_NOT_FOUND(8001, "资源不存在"),
    RESOURCE_ALREADY_EXISTS(8002, "资源已存在"),
    INTERNAL_ERROR(8003, "内部错误");
    
    private final Integer code;
    private final String message;
    
    ErrorCode(Integer code, String message) {
        this.code = code;
        this.message = message;
    }
    
    public Integer getCode() {
        return code;
    }
    
    public String getMessage() {
        return message;
    }
}
