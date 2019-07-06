package com.xie.java.datasource.interceptor;

import com.xie.java.datasource.DataSourceProperties;
import com.xie.java.datasource.RouteContextManager;
import org.apache.ibatis.executor.Executor;
import org.apache.ibatis.mapping.MappedStatement;
import org.apache.ibatis.plugin.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.sql.DataSource;
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
    private final Logger logger = LoggerFactory.getLogger(getClass());
    private DataSource dataSource;
    private Map<String, DataSourceProperties> datasourceProperties;

    public UpdateInterceptor(DataSource dataSource,Map<String, DataSourceProperties> properties){
        this.dataSource = dataSource;
        this.datasourceProperties = properties;
    }

    @Override
    public Object intercept(Invocation invocation) throws Throwable {
        logger.debug("更新操作");
        MappedStatement ms = (MappedStatement) invocation.getArgs()[0];
        DataSourceProperties dsPr = datasourceProperties.get(RouteContextManager.currentDatabaseId());
        if (!dsPr.isMaster()) {
            logger.warn("更新操作强制从库[{}]切换到主库[{}]", dsPr.getId(), dsPr.getParentId());
            RouteContextManager.setCurrentDatabaseId(dsPr.getParentId(),RouteContextManager.hasTransaction());
        }
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
