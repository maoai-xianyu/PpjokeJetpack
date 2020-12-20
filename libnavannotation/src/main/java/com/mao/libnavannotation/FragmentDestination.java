package com.mao.libnavannotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Target;

/**
 * @author zhangkun
 * @time 2020/9/20 10:50 PM
 * @Description
 */
@Target(ElementType.TYPE)
public @interface FragmentDestination {

    // 调转链接
    String pageUrl();
    // 是否需要登录
    boolean needLogin() default false;
    // 是否作为第一个页面
    boolean asStarter() default false;

}
