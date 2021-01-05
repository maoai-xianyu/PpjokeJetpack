package com.example.libnetwork;

/**
 * @author zhangkun
 * @time 2020/12/22 9:59 AM
 * @Description
 */
public class ApiResponse<T> {

    public boolean success;
    public int status;
    public String message;
    public T body;
}

