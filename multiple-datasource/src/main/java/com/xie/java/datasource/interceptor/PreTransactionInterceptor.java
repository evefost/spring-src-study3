package com.xie.java.datasource.interceptor;

import com.xie.java.datasource.TransactionContextHolder;
import org.aopalliance.intercept.MethodInvocation;
import org.springframework.transaction.interceptor.TransactionInterceptor;

/**
 * Created by xieyang on 19/7/4.
 */
public class PreTransactionInterceptor extends TransactionInterceptor {


    @Override
    public Object invoke(MethodInvocation invocation) throws Throwable {
        int increase = TransactionContextHolder.increase();
        String databaseId = TransactionContextHolder.getDatabaseId(invocation.getMethod());
        TransactionContextHolder.setCurrentDatabaseId(databaseId);
        if (increase == 1) {
            System.out.println("进入事务拦截器");
        }
        try {
            return super.invoke(invocation);
        } finally {
            int decrease = TransactionContextHolder.decrease();
            TransactionContextHolder.setCurrentDatabaseId(null);
            if (decrease == 0) {
                System.out.println("退出事务");
            }
        }
    }
}
