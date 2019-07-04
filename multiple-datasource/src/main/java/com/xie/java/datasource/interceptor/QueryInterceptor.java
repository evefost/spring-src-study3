package com.xie.java.datasource.interceptor;

import com.xie.java.datasource.DataSourceProperties;
import com.xie.java.datasource.ServiceContextHolder;
import com.xie.java.datasource.TransactionContextHolder;
import org.apache.ibatis.cache.CacheKey;
import org.apache.ibatis.executor.Executor;
import org.apache.ibatis.mapping.BoundSql;
import org.apache.ibatis.mapping.MappedStatement;
import org.apache.ibatis.plugin.*;
import org.apache.ibatis.session.ResultHandler;
import org.apache.ibatis.session.RowBounds;

import java.util.Map;
import java.util.Properties;
import java.util.Random;

/**
 * {@link Executor}
 * Created by xieyang on 19/6/25.
 */

@Intercepts({@Signature(
        type = Executor.class,
        method = "query",
        args = {MappedStatement.class, Object.class, RowBounds.class, ResultHandler.class}
), @Signature(
        type = Executor.class,
        method = "query",
        args = {MappedStatement.class, Object.class, RowBounds.class, ResultHandler.class, CacheKey.class, BoundSql.class}
)})
public class QueryInterceptor implements Interceptor {

    private Map<String, DataSourceProperties> datasourceProperties;

    public QueryInterceptor(Map<String, DataSourceProperties> properties){
        this.datasourceProperties = properties;
    }

    Random r = new Random();

    @Override
    public Object intercept(Invocation invocation) throws Throwable {
        MappedStatement ms = (MappedStatement) invocation.getArgs()[0];

        String databaseId = ServiceContextHolder.currentDatabaseId();
        DataSourceProperties dsPr = datasourceProperties.get(databaseId);
        if(dsPr.getSlavers() != null && dsPr.getSlavers().size()>0){
            //主库查询自动切换成从库
            DataSourceProperties slDs = dsPr.getSlavers().get(r.nextInt(dsPr.getSlavers().size()));
            ServiceContextHolder.setCurrentDatabaseId(slDs.getId());
        }

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
