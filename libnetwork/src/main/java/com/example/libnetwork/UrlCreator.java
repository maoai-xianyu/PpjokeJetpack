package com.example.libnetwork;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.Map;

/**
 * @author zhangkun
 * @time 2020/12/22 10:13 AM
 * @Description 拼接请求的url
 */
public class UrlCreator {

    public static String createUrlFromParams(String url, Map<String, Object> params) {

        StringBuilder builder = new StringBuilder();
        builder.append(url);
        // 判断url的规范
        if (url.indexOf("?") > 0 || url.indexOf("&") > 0) {
            builder.append("&");
        } else {
            builder.append("?");
        }
        // 拼接参数
        for (Map.Entry<String, Object> entry : params.entrySet()) {
            try {
                String value = URLEncoder.encode(String.valueOf(entry.getValue()), "UTF-8");
                builder.append(entry.getKey()).append("=").append(value).append("&");
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }

        }
        // 删除多出来的 & 符号
        builder.deleteCharAt(builder.length() - 1);
        return builder.toString();
    }
}
