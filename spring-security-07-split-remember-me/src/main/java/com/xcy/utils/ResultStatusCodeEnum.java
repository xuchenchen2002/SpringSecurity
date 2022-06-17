package com.xcy.utils;

/**
 * @author 小晨Yu呀!
 * @time 2022-06-15
 */
public enum ResultStatusCodeEnum {

    //成功
    SUCCESS(200,"请求成功"),

    //失败
    FAIL(400,"请求失败"),

    //未认证（签名错误）
    UNAUTHORIZED(401,"未认证"),

    //接口不存在
    NOT_FOUND(404,"接口不存在"),

    //服务器内部错误
    INTERNAL_SERVER_ERROR(500,"服务器内部异常");

    public Integer code;
    public String msg;
    ResultStatusCodeEnum(int code,String msg) {
        this.code = code;
        this.msg = msg;
    }

}
