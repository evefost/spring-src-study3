package com.xie.java.datasource;

import org.aopalliance.intercept.MethodInvocation;
import org.springframework.transaction.interceptor.TransactionInterceptor;

/**
 * Created by xieyang on 19/7/4.
 */
public class MyTransactionInterceptor extends TransactionInterceptor {
    @Override
    public Object invoke(MethodInvocation invocation) throws Throwable {
        System.out.println("进入事务拦截器");
        return super.invoke(invocation);
    }
}
