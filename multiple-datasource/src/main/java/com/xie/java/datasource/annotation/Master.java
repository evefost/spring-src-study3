package com.xie.java.datasource.annotation;

import java.lang.annotation.*;

/**
 *
 * 用于强制路由到某数
 * Created by xieyang on 19/6/22.
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
@Documented
public @interface Master {

    /**
     * 默认值为路由到黑认主库
     * @return
     */
    String id() default "";

}
