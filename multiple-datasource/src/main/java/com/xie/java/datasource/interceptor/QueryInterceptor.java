package com.xie.java.datasource.interceptor;

import com.xie.java.datasource.DataSourceProperties;
import com.xie.java.datasource.DynamicRoutingStatementHandler;
import com.xie.java.datasource.RouteContextManager;
import org.apache.ibatis.cache.CacheKey;
import org.apache.ibatis.executor.Executor;
import org.apache.ibatis.executor.statement.RoutingStatementHandler;
import org.apache.ibatis.mapping.BoundSql;
import org.apache.ibatis.mapping.MappedStatement;
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

    private final Logger logger = LoggerFactory.getLogger(getClass());

    private DataSource dataSource;

    private Map<String, DataSourceProperties> datasourceProperties;

    public QueryInterceptor(DataSource dataSource, Map<String, DataSourceProperties> properties) {
        this.dataSource = dataSource;
        this.datasourceProperties = properties;
    }

    Random r = new Random();

    @Override
    public Object intercept(Invocation invocation) throws Throwable {
        MappedStatement ms = (MappedStatement) invocation.getArgs()[0];
        RouteContextManager.setMapStatement(ms);
        String databaseId = RouteContextManager.currentDatabaseId();
        DataSourceProperties dsPr = datasourceProperties.get(databaseId);
        if (RouteContextManager.hasTransaction()) {
            if (!dsPr.isMaster()) {
                DataSourceProperties mDs = datasourceProperties.get(dsPr.getParentId());
                logger.debug("有事务查询操作slaver:{} 切换到 master:{}", dsPr.getId(), mDs.getId());
            }
        } else if (dsPr.getSlavers() != null && dsPr.getSlavers().size() > 0) {
            DataSourceProperties slDs = dsPr.getSlavers().get(r.nextInt(dsPr.getSlavers().size()));
            RouteContextManager.setCurrentDatabaseId(slDs.getId(),false);
            logger.debug("无事务查询操作master:{} 切换到 slaver[{}]:{}", dsPr.getId(), dsPr.getSlavers().size(), slDs.getId());
        }
        RouteContextManager.setCurrentDatabaseId("ds1",true);
        try {
            return invocation.proceed();
        }finally {
            RouteContextManager.removeMapStatement();
            //TransactionSynchronizationManager.bindResource(this.dataSource,txObject);
        }
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
