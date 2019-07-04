package com.xie.java.datasource;

import org.springframework.core.NamedThreadLocal;

/**
 * Created by xieyang on 19/6/27.
 */
public class RoutingContext {

    private static final ThreadLocal<String> databaseId =
            new NamedThreadLocal<>("Current databaseId");

}
