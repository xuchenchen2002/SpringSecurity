package com.xcy.domain;

/**
 * @author 小晨Yu呀!
 * @time 2022-05-03
 */
public class Result<T> {

    // 请求结果的状态码，比如 200，404，...
    private Integer status;
    // 响应结果的信息提示，比如 没有权限访问，您为登录，...
    private String msg;
    // 响应结果的数据，比如 查询出了所有用户的数据
    private T data;

    public Result() { }

    public Result(Integer status, String msg, T data) {
        this.status = status;
        this.msg = msg;
        this.data = data;
    }
    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }
}
