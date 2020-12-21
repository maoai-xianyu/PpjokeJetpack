package com.example.libnetwork;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.util.HashMap;

import androidx.annotation.IntDef;

/**
 * @author zhangkun
 * @time 2020/12/21 10:17 AM
 * @Description
 *
 * T 是 response 的实体类型
 * R 是 Request 的子类
 */
public class Request<T,R extends Request> {

    private String mUrl;
    // 存储请求的header
    private final HashMap<String,String> headers = new HashMap<>();
    // 请求的参数存储
    private final HashMap<String,Object> params = new HashMap<>();

    //仅仅只访问本地缓存，即便本地缓存不存在，也不会发起网络请求
    public static final int CACHE_ONLY = 1;
    //先访问缓存，同时发起网络的请求，成功后缓存到本地 并发
    public static final int CACHE_FIRST = 2;
    //仅仅只访问服务器，不存任何存储
    public static final int NET_ONLY = 3;
    //先访问网络，成功后缓存到本地
    public static final int NET_CACHE = 4;


    // 使用注解标记类型
    @IntDef({CACHE_ONLY, CACHE_FIRST, NET_CACHE, NET_ONLY})
    @Retention(RetentionPolicy.SOURCE)
    public @interface CacheStrategy {

    }


    public Request(String url) {

        // user/list
        this.mUrl = url;
    }
}
