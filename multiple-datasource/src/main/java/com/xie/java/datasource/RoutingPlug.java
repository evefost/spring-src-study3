package com.xie.java.datasource;

import org.apache.ibatis.plugin.Interceptor;
import org.apache.ibatis.plugin.Invocation;
import org.springframework.transaction.interceptor.TransactionAspectSupport;

import java.util.Properties;

/**
 * Created by xieyang on 19/6/25.
 */
public class RoutingPlug implements Interceptor {

    @Override
    public Object intercept(Invocation invocation) throws Throwable {
        System.out.printf("xxxxxxx");
        TransactionAspectSupport.currentTransactionStatus();
        return invocation;
    }

    @Override
    public Object plugin(Object target) {
        System.out.printf("6666666666666");
        return target;
    }

    @Override
    public void setProperties(Properties properties) {

    }
}
