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

    String pageUrl();

    boolean needLogin() default false;

    boolean asStarter() default false;

}
