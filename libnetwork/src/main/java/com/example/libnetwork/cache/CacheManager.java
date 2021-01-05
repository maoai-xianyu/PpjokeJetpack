package com.example.libnetwork.cache;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

/**
 * @author zhangkun
 * @time 2020/12/31 10:21 AM
 * @Description 缓存管理类
 */
public class CacheManager {


    //反序列,把二进制数据转换成java object对象
    private static Object toObject(byte[] data) {

        ByteArrayInputStream bois = null;
        ObjectInputStream oos = null;

        try {
            bois = new ByteArrayInputStream(data);
            oos = new ObjectInputStream(bois);
            return oos.readObject();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                if (bois != null) {
                    bois.close();
                }
                if (oos != null) {
                    oos.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return null;
    }

    //序列化存储数据需要转换成二进制
    private static <T> byte[] toByteArray(T body) {
        ByteArrayOutputStream baos = null;
        ObjectOutputStream oos = null;
        try {
            baos = new ByteArrayOutputStream();
            oos = new ObjectOutputStream(baos);
            oos.writeObject(body);
            oos.flush();
            return baos.toByteArray();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                if (baos != null) {
                    baos.close();
                }
                if (oos != null) {
                    oos.close();
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return new byte[0];
    }

    public static <T> void delete(String key, T body) {
        Cache cache = new Cache();
        cache.key = key;
        cache.data = toByteArray(body);
        CacheDataBase.get().getCache().delete(cache);
    }

    public static <T> void save(String key, T body) {
        Cache cache = new Cache();
        cache.key = key;
        cache.data = toByteArray(body);
        CacheDataBase.get().getCache().save(cache);
    }


    // 获取缓存数据
    public static Object getCache(String key) {
        Cache cache = CacheDataBase.get().getCache().getCache(key);
        if (cache != null && cache.data != null) {
            return toObject(cache.data);
        }
        return null;
    }


}
