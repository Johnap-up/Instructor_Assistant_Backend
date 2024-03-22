package org.example.entity;

import com.alibaba.fastjson2.JSONObject;
import com.alibaba.fastjson2.JSONWriter;

public record RestBean<T> (int code, T data, String message) {
    //写几个工具方法，用于快速创建RestBean对象
    public static <T> RestBean<T> success(T data){
        return new RestBean<>(200, data, "登录成功");
    }
    public static <T> RestBean<T> success(){ return success(null); }
    public static <T> RestBean<T> unauthorized(String message){ return failure(401, message); }
    public static <T> RestBean<T> forbidden(String message){ return failure(403, message); }
    public static <T> RestBean<T> failure(int code, String message){
        return new RestBean<>(code, null, message);
    }
    public String asJsonString() {
        return JSONObject.toJSONString(this, JSONWriter.Feature.WriteNulls);
    }
}