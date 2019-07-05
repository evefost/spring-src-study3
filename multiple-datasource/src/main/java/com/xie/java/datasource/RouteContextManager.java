package com.xie.java.datasource;

import java.lang.reflect.Method;
import java.util.Map;

/**
 *
 * @author xieyang
 * @date 19/6/27
 */
public class RouteContextManager {

    private static Map<Method, DatasourceConfig.MethodMapping> methodDatabaseMapping;


    private static ContextHolder serviceContextHolder = new ContextHolder();


    private static ContextHolder transactionContextHolder = new ContextHolder();


    public static int increase(boolean transaction) {
        if(transaction){
            return transactionContextHolder.increase();
        }else {
            return serviceContextHolder.increase();
        }

    }

    public static int decrease(boolean transaction) {
        if(transaction){
            return transactionContextHolder.decrease();
        }else {
            return serviceContextHolder.decrease();
        }
    }


    public static void setCurrentDatabaseId(String databaseId,boolean transaction){
        if(transaction){
            transactionContextHolder.setCurrentDatabaseId(databaseId);
        }else {
            serviceContextHolder.setCurrentDatabaseId(databaseId);
        }
    }

    public static String getDatabaseId(Method method){
        DatasourceConfig.MethodMapping methodMapping = methodDatabaseMapping.get(method);
        if(methodMapping== null){
            return null;
        }
        return methodMapping.getDatabaseId();
    }


    public static void setMethodDatabaseMapping(Map<Method, DatasourceConfig.MethodMapping> mapping){
        methodDatabaseMapping = mapping;
    }

    public static String currentDatabaseId() {
      return   serviceContextHolder.currentDatabaseId();
    }

    public static boolean hasTransaction() {
        return transactionContextHolder.counterValue() != 0;
    }

}
