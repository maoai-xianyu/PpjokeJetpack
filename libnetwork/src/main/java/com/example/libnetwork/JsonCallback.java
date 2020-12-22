package com.example.libnetwork;

/**
 * @author zhangkun
 * @time 2020/12/22 9:58 AM
 * @Description
 */
public abstract class JsonCallback<T> {

    public void onSuccess(ApiResponse<T> response) {

    }

    public void onError(ApiResponse<T> response) {

    }

    public void onCacheSuccess(ApiResponse<T> response) {

    }

}
