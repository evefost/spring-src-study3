package com.xie.java.datasource.interceptor;

import com.xie.java.datasource.RouteContextManager;
import org.aopalliance.intercept.MethodInvocation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.transaction.interceptor.TransactionInterceptor;

/**
 *
 * @author xieyang
 * @date 19/7/4
 */
public class PreTransactionInterceptor extends TransactionInterceptor {
    public final Logger logger = LoggerFactory.getLogger(getClass());

    @Override
    public Object invoke(MethodInvocation invocation) throws Throwable {
        int increase = RouteContextManager.increase(true);
        String databaseId = RouteContextManager.getDatabaseId(invocation.getMethod());
        RouteContextManager.setCurrentDatabaseId(databaseId,true);
        if (increase == 1) {
            logger.debug("进入事务拦截器");
        }
        try {
            return super.invoke(invocation);
        } finally {
            int decrease = RouteContextManager.decrease(true);
            RouteContextManager.setCurrentDatabaseId(null,true);
            if (decrease == 0) {
                logger.debug("退出事务");
            }
        }
    }
}
