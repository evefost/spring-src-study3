package com.xie.java.datasource.interceptor;

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

    public ExecutorInterceptor() {
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
        }
        logger.debug(" {} bind databaseId [{}]",ms.getId(),bindId);

        String masterId =  null;
        if(RouteContextManager.isMaster(currentId)){
            masterId =  currentId;

        }else {
            masterId =  RouteContextManager.getMasterId(currentId);
        }
        String slaverId = RouteContextManager.getSlaverId(masterId);
        boolean isSelect  =  SqlCommandType.SELECT.equals(ms.getSqlCommandType());
        String tip = "";
        if(RouteContextManager.hasTransaction()){
            RouteContextManager.setCurrentDatabaseId(masterId,true);
            tip = "有事务";
        }else {
            if(!RouteContextManager.hadUpdateBefore() && isSelect){
                //查询操作且之前没有过更新操作
                RouteContextManager.setCurrentDatabaseId(slaverId,false);
                tip = "无事务";
            }else {
                RouteContextManager.setCurrentDatabaseId(masterId,false);
                if(isSelect){
                    logger.debug("无事务查询操作，之前有过修改小操作,切换到主库:[{}]",masterId);
                    tip = "无事务,之前有过修改小操作";
                } else {
                    tip = "无事务更新";
                }
            }
        }
        boolean master = RouteContextManager.isMaster(RouteContextManager.currentDatabaseId());
        if(!isSelect){
            RouteContextManager.markUpdateOperateFlag();
        }
        logger.info("[{}]应连接[{}][{}]({})", ms.getSqlCommandType(), master ? "主库" : "从库", RouteContextManager.currentDatabaseId(), tip);
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
