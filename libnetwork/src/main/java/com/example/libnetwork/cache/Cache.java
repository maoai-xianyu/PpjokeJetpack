package com.example.libnetwork.cache;

import java.io.Serializable;

import androidx.annotation.NonNull;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

/**
 * @author zhangkun
 * @time 2020/12/28 10:13 AM
 * @Description 缓存的时候需要实现 Serializable 序列化
 */
@Entity(tableName = "cache")
public class Cache implements Serializable {
    //PrimaryKey 必须要有,且不为空,autoGenerate 主键的值是否由Room自动生成,默认false
    @PrimaryKey(autoGenerate = false)
    @NonNull // 不能为空
    //@Ignore 可以忽略字段，不再表中生成对应的字段
    public String key;

    // byte[] 因为每次返回的数据都是不一样的，转换二进制存储到数据库中
    // @ColumnInfo(name = "_data") // ColumnInfo 为数据库表中的字段命名，
    // 如果没有就用属性data命名,默认情况可以不用写
    public byte[] data;
}
