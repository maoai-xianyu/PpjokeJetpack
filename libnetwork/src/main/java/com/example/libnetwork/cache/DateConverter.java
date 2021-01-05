package com.example.libnetwork.cache;

import java.util.Date;

import androidx.room.TypeConverter;

/**
 * @author zhangkun
 * @time 2020/12/31 10:07 AM
 * @Description TypeConverter
 */
public class DateConverter {

    @TypeConverter
    public static Long date2Long(Date date) {
        return date.getTime();
    }

    @TypeConverter
    public static Date long2Date(Long data) {
        return new Date(data);
    }

}
