package com.example.libnetwork;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.reflect.Field;
import java.lang.reflect.Type;
import java.util.HashMap;
import java.util.Map.Entry;

import androidx.annotation.IntDef;
import okhttp3.Call;
import okhttp3.Request.Builder;

/**
 * @author zhangkun
 * @time 2020/12/21 10:17 AM
 * @Description T 是 response 的实体类型
 * R 是 Request 的子类
 */
public abstract class Request<T, R extends Request> {

    protected String mUrl;
    // 存储请求的header
    protected final HashMap<String, String> headers = new HashMap<>();
    // 请求的参数存储
    protected final HashMap<String, Object> params = new HashMap<>();

    //仅仅只访问本地缓存，即便本地缓存不存在，也不会发起网络请求
    public static final int CACHE_ONLY = 1;
    //先访问缓存，同时发起网络的请求，成功后缓存到本地 并发
    public static final int CACHE_FIRST = 2;
    //仅仅只访问服务器，不存任何存储
    public static final int NET_ONLY = 3;
    //先访问网络，成功后缓存到本地
    public static final int NET_CACHE = 4;
    private String cacheKey;
    private Type mType;

    // 使用注解标记类型
    @IntDef({CACHE_ONLY, CACHE_FIRST, NET_CACHE, NET_ONLY})
    @Retention(RetentionPolicy.SOURCE)
    public @interface CacheStrategy {

    }


    public Request(String url) {

        // user/list
        this.mUrl = url;
    }

    // 添加请求头
    public R addHeader(String key, String value) {
        headers.put(key, value);
        return (R) this;
    }

    // 添加参数
    public R addParams(String key, Object value) {
        if (value == null) {
            return (R) this;
        }
        // 八种基本类型  byte short int long float double boolean char
        // 如何判断，
        // 方式一：可以用 value instanceof Integer 的方式
        // 方式二：反射 获取基本类型中的 TYPE 字段
        // eg: public static final Class<Double>   TYPE = (Class<Double>) Class.getPrimitiveClass("double");

        try {
            Field field = value.getClass().getField("TYPE");
            Class clazz = (Class) field.get(null);
            // 判断 clazz 是不是基本类型
            if (clazz.isPrimitive()) {
                params.put(key, value);
            }
        } catch (NoSuchFieldException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
        return (R) this;
    }

    // 允许指定 cacheKey
    public R cacheKey(String key) {
        this.cacheKey = key;
        return (R) this;
    }

    public R responseType(Type type) {
        mType = type;
        return (R) this;
    }

    public R responseType(Class claz) {
        mType = claz;
        return (R) this;
    }

    // 同步
    public void execute() {

    }

    // 异步
    public void execute(JsonCallback<T> callback) {
        getCall();
    }

    private Call getCall() {
        okhttp3.Request.Builder builder = new okhttp3.Request.Builder();
        // 为 okhttp3 添加请求头
        addHeaders(builder);
        // 构造 requestBody  GET POST 需要通过子类构建
        okhttp3.Request request = generateRequest(builder);
        Call call = ApiService.okHttpClient.newCall(request);
        return call;
    }

    protected abstract okhttp3.Request generateRequest(Builder builder);

    private void addHeaders(okhttp3.Request.Builder builder) {
        for (Entry<String, String> entry : headers.entrySet()) {
            builder.addHeader(entry.getKey(), entry.getValue());
        }
    }
}
