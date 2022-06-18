package com.xcy.utils;

/**
 * @author 小晨Yu呀!
 * @time 2022-06-15
 */
public enum ResultStatusCodeEnum {

    //成功
    SUCCESS(200, "请求成功"),
    OK(200, "请求成功"),

    //失败
    FAIL(400, "请求失败"),
    BAD_REQUEST(400, "请求失败"),

    // 未认证（签名错误）
    UNAUTHORIZED(401, "未认证"),

    // 需要支付
    PAYMENT_REQUIRED(402, "需要支付"),

    //403 被拒绝状态
    FORBIDDEN(403, "权限不足"),

    //接口不存在
    NOT_FOUND(404, "请求资源不存在"),

    METHOD_NOT_ALLOWED(405, "请求方法不被允许"),

    NOT_ACCEPTABLE(406, "匿名访问"),

    REQUEST_TIMEOUT(408, "请求超时"),

    //服务器内部错误
    INTERNAL_SERVER_ERROR(500, "服务器内部异常"),

    NOT_IMPLEMENTED(501, "方法没有具体的实现"),

    BAD_GATEWAY(502, "错误的网关"),

    // 暂停服务（接口暂停服务）
    SERVICE_UNAVAILABLE(503, "接口暂停服务"),

    GATEWAY_TIMEOUT(504, "网关超时");


    // 访问成功、响应成功
    int SC_OK = 200;
    // 错误的请求（请求失败）
    int SC_BAD_REQUEST = 400;
    // 未授权访问
    int SC_UNAUTHORIZED = 401;
    // 需要支付
    int SC_PAYMENT_REQUIRED = 402;
    // 权限不足
    int SC_FORBIDDEN = 403;
    // 未找到请求资源
    int SC_NOT_FOUND = 404;
    // 请求方法不被允许
    int SC_METHOD_NOT_ALLOWED = 405;
    // 匿名访问
    int SC_NOT_ACCEPTABLE = 406;
    // 请求超时
    int SC_REQUEST_TIMEOUT = 408;
    // 服务器内部异常
    int SC_INTERNAL_SERVER_ERROR = 500;
    // 方法没有具体的实现
    int SC_NOT_IMPLEMENTED = 501;
    // 错误的网关
    int SC_BAD_GATEWAY = 502;
    // 暂停服务（接口暂不被使用）
    int SC_SERVICE_UNAVAILABLE = 503;
    // 网关超时
    int SC_GATEWAY_TIMEOUT = 504;


    public Integer code;
    public String msg;

    ResultStatusCodeEnum(int code, String msg) {
        this.code = code;
        this.msg = msg;
    }

}
