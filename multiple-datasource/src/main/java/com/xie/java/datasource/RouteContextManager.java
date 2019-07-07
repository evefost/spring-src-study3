package com.xie.java.datasource;

import org.apache.ibatis.mapping.MappedStatement;

import java.lang.reflect.Method;
import java.util.List;
import java.util.Map;
import java.util.Random;

/**
 * @author xieyang
 * @date 19/6/27
 */
public class RouteContextManager {

    private static Map<Method, DatasourceConfig.MethodMapping> methodDatabaseMapping;
    private static MultipleSourceProperties multipleSourceProperties;

    private static ContextHolder serviceContextHolder = new ContextHolder();


    private static ContextHolder transactionContextHolder = new ContextHolder();

    private static ThreadLocal<MappedStatement> currentMapStatement = ThreadLocal.withInitial(() -> null);

    private static ThreadLocal<Boolean> hadhUpdateOperateBefore = ThreadLocal.withInitial(() -> false);



    public static void setMultipleSourceProperties(MultipleSourceProperties multipleSourceProperties) {
        RouteContextManager.multipleSourceProperties = multipleSourceProperties;
    }

    public static void setMapStatement(MappedStatement statement) {
        currentMapStatement.set(statement);
    }

    public static void removeMapStatement() {
        currentMapStatement.remove();
    }


    public static MappedStatement getMapStatement() {
        return currentMapStatement.get();
    }


    public static int increase(boolean transaction) {
        if (transaction) {
            return transactionContextHolder.increase();
        } else {
            return serviceContextHolder.increase();
        }

    }

    public static int decrease(boolean transaction) {
        if (transaction) {
            return transactionContextHolder.decrease();
        } else {
            return serviceContextHolder.decrease();
        }
    }


    public static void setCurrentDatabaseId(String databaseId, boolean transaction) {
        if (transaction) {
            transactionContextHolder.setCurrentDatabaseId(databaseId);
        } else {
            serviceContextHolder.setCurrentDatabaseId(databaseId);
        }
    }

    public static String getDatabaseId(Method method) {
        DatasourceConfig.MethodMapping methodMapping = methodDatabaseMapping.get(method);
        if (methodMapping == null) {
            return null;
        }
        return methodMapping.getDatabaseId();
    }


    public static void setMethodDatabaseMapping(Map<Method, DatasourceConfig.MethodMapping> mapping) {
        methodDatabaseMapping = mapping;
    }

    public static String currentDatabaseId() {
        if (hasTransaction()) {
            return transactionContextHolder.currentDatabaseId();
        }
        return serviceContextHolder.currentDatabaseId();
    }

    public static boolean hasTransaction() {
        return transactionContextHolder.counterValue() != 0;
    }


    public static boolean isMaster(String databaseId) {
        DataSourceProperties dataSourceProperties = multipleSourceProperties.getDatasourceProperties().get(databaseId);
        return dataSourceProperties.getParentId() == null;
    }

    public static String getMasterId(String slaverId) {
        DataSourceProperties dataSourceProperties = multipleSourceProperties.getDatasourceProperties().get(slaverId);
        return dataSourceProperties.getParentId();
    }

    private static Random r = new Random();

    public static String getSlaverId(String masterId) {
        DataSourceProperties dataSourceProperties = multipleSourceProperties.getDatasourceProperties().get(masterId);
        List<DataSourceProperties> slavers = dataSourceProperties.getSlavers();
        if (slavers.isEmpty()) {
            return null;
        }
        if (slavers.size() == 1) {
            return slavers.get(0).getId();
        }
        return slavers.get(r.nextInt(slavers.size())).getId();
    }

    public static String getDefaultDatabaseId() {
        return multipleSourceProperties.getDefaultDatabaseId();
    }


    public static  void markUpdateOperateFlag(){
        hadhUpdateOperateBefore.set(true);
    }

    public static  void removeUpdateOperateFlag(){
        hadhUpdateOperateBefore.set(false);
    }

    public static  boolean hadUpdateBefore(){
       return hadhUpdateOperateBefore.get();
    }

}
