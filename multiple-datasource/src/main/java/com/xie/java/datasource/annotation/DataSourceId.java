package com.xie.java.datasource.annotation;

import java.lang.annotation.*;

/**
 *
 * 据数据源id指定数据源
 * Created by xieyang on 19/6/22.
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
@Documented
public @interface DataSourceId {
    String id();
}
