package com.example.libnetwork.cache;

import java.io.Serializable;

import androidx.room.Entity;

/**
 * @author zhangkun
 * @time 2020/12/31 9:35 AM
 * @Description 用于测试外键
 */
@Entity(tableName = "user")
public class User implements Serializable {
    public String userName;
    public long id;

}
