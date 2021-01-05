package com.example.libnetwork;

import okhttp3.Request.Builder;

/**
 * @author zhangkun
 * @time 2020/12/22 10:12 AM
 * @Description Get 请求
 */
public class GetRequest<T> extends Request<T, GetRequest<T>> {

    public GetRequest(String url) {
        super(url);
    }

    @Override
    protected okhttp3.Request generateRequest(Builder builder) {
        //get 请求把参数拼接在 url后面
        String url = UrlCreator.createUrlFromParams(mUrl, params);
        okhttp3.Request request = builder.get().url(url).build();
        return request;
    }
}
