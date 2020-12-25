package com.example.libnetwork;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;

import java.lang.reflect.Type;

/**
 * @author zhangkun
 * @time 2020/12/23 10:09 AM
 * @Description 默认的转换
 */
public class JsonConvertNew<T> implements Convert<T> {

    @Override
    public T convert(String response, Type type) {

        JSONObject jsonObject = JSON.parseObject(response);
        JSONObject data = jsonObject.getJSONObject("data");
        if (data != null) {
            Object data1 = data.get("data");
            return JSON.parseObject(data1.toString(), type);
        }
        return null;
    }

    @Override
    public T convert(String response, Class clazz) {

        JSONObject jsonObject = JSON.parseObject(response);
        JSONObject data = jsonObject.getJSONObject("data");
        if (data != null) {
            Object data1 = data.get("data");
            return (T) JSON.parseObject(data1.toString(), clazz);

        }
        return null;
    }
}
