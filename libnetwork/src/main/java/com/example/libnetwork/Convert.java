package com.example.libnetwork;

import java.lang.reflect.Type;

/**
 * @author zhangkun
 * @time 2020/12/23 9:56 AM
 * @Description 类型和数据类的转换
 */
public interface Convert<T> {

    T convert(String response, Type type);

    T convert(String response, Class clazz);

}
