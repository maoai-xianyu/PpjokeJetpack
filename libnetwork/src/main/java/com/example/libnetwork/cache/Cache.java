package com.example.libnetwork.cache;

import java.io.Serializable;

/**
 * @author zhangkun
 * @time 2020/12/28 10:13 AM
 * @Description 缓存的时候需要实现 Serializable 序列化
 */
public class Cache implements Serializable {
    public String key;
    // byte[] 因为每次返回的数据都是不一样的，转换二进制存储到数据库中
    public byte[] data;

}
