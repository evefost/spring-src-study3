package com.xie.java.datasource.interceptor;

import com.xie.java.datasource.DataSourceProperties;
import com.xie.java.datasource.TransactionContextHolder;
import org.apache.ibatis.executor.Executor;
import org.apache.ibatis.mapping.MappedStatement;
import org.apache.ibatis.plugin.*;

import java.util.Map;
import java.util.Properties;

/**
 * {@link Executor}
 * Created by xieyang on 19/6/25.
 */

@Intercepts({@Signature(
        type = Executor.class,
        method = "update",
        args = {MappedStatement.class, Object.class})})
public class UpdateInterceptor implements Interceptor {

    private Map<String, DataSourceProperties> datasourceProperties;

    public UpdateInterceptor(Map<String, DataSourceProperties> properties){
        this.datasourceProperties = properties;
    }
    @Override
    public Object intercept(Invocation invocation) throws Throwable {
        MappedStatement ms = (MappedStatement) invocation.getArgs()[0];
        System.out.println("是否有事务:" + TransactionContextHolder.hasTransaction() + "  bind databaseId:" + ms.getDatabaseId());
        return invocation.proceed();
    }

    @Override
    public Object plugin(Object target) {
        return Plugin.wrap(target, this);
    }

    @Override
    public void setProperties(Properties properties) {

    }
}
