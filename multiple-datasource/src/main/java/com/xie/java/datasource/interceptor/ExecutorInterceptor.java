package com.xie.java.datasource.interceptor;

import com.xie.java.datasource.DataSourceProperties;
import com.xie.java.datasource.DynamicRoutingStatementHandler;
import com.xie.java.datasource.RouteContextManager;
import org.apache.ibatis.cache.CacheKey;
import org.apache.ibatis.executor.Executor;
import org.apache.ibatis.executor.statement.RoutingStatementHandler;
import org.apache.ibatis.mapping.BoundSql;
import org.apache.ibatis.mapping.MappedStatement;
import org.apache.ibatis.mapping.SqlCommandType;
import org.apache.ibatis.plugin.*;
import org.apache.ibatis.session.ResultHandler;
import org.apache.ibatis.session.RowBounds;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.sql.DataSource;
import java.util.Map;
import java.util.Properties;
import java.util.Random;

/**
 * 该拦截在事务之后,获取连接之前
 * {@link Executor}
 * Created by xieyang on 19/6/25.
 */

@Intercepts({
        @Signature(
                type = Executor.class,
                method = "update",
                args = {MappedStatement.class, Object.class}),
        @Signature(
                type = Executor.class,
                method = "query",
                args = {MappedStatement.class, Object.class, RowBounds.class, ResultHandler.class}
        ),
        @Signature(
                type = Executor.class,
                method = "query",
                args = {MappedStatement.class, Object.class, RowBounds.class, ResultHandler.class, CacheKey.class, BoundSql.class}
        )})
public class ExecutorInterceptor implements Interceptor {

    private final Logger logger = LoggerFactory.getLogger(getClass());

    private DataSource dataSource;

    private Map<String, DataSourceProperties> datasourceProperties;

    public ExecutorInterceptor(DataSource dataSource, Map<String, DataSourceProperties> properties) {
        this.dataSource = dataSource;
        this.datasourceProperties = properties;
    }

    Random r = new Random();

    @Override
    public Object intercept(Invocation invocation) throws Throwable {
        MappedStatement ms = (MappedStatement) invocation.getArgs()[0];
        calculateDatasource(ms);
        RouteContextManager.setMapStatement(ms);
        try {
            return invocation.proceed();
        } finally {
            RouteContextManager.removeMapStatement();
        }
    }

    private void calculateDatasource(MappedStatement ms){
        String databaseId = RouteContextManager.currentDatabaseId();
        String bindId = ms.getDatabaseId();
        String currentId = databaseId;
        if(bindId != null){
            currentId = bindId;
            logger.debug(" mapstatement bind [{}]",bindId);
        }

        String masterId =  null;
        if(RouteContextManager.isMaster(currentId)){
            masterId =  currentId;

        }else {
            masterId =  RouteContextManager.getMasterId(currentId);
        }
        String slaverId = RouteContextManager.getSlaverId(masterId);

        if(RouteContextManager.hasTransaction()){
            RouteContextManager.setCurrentDatabaseId(masterId,true);
        }else {
            if(SqlCommandType.SELECT.equals(ms.getSqlCommandType())){
                //todo
                RouteContextManager.setCurrentDatabaseId(slaverId,false);
            }else {
                RouteContextManager.setCurrentDatabaseId(masterId,false);
            }
        }
        logger.info("[{}]当前应选数据源[{}]",ms.getSqlCommandType(), RouteContextManager.currentDatabaseId());
    }

    @Override
    public Object plugin(Object target) {
        if (target instanceof RoutingStatementHandler) {
            target = new DynamicRoutingStatementHandler((RoutingStatementHandler) target);
        }
        return Plugin.wrap(target, this);
    }

    @Override
    public void setProperties(Properties properties) {

    }
}
