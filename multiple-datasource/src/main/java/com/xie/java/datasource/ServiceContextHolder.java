package com.xie.java.datasource;

import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by xieyang on 19/6/27.
 */
public class ServiceContextHolder {

   public static Map<Method, DatasourceConfig.MethodMapping> methodDatabaseMapping;
    /**
     * 事务计数器
     */
    public static ThreadLocal<TransactionCounter> transactionCounter = ThreadLocal.withInitial(() -> new TransactionCounter());



    public static ThreadLocal<Map<Integer,String>> currentDatabaseId = ThreadLocal.withInitial(() -> new HashMap<>());


    public static void setCurrentDatabaseId(String databaseId) {
        Map<Integer, String> currentDatabase = currentDatabaseId.get();
        if (databaseId == null) {
            currentDatabase.remove(transactionCounter.get().value());
        } else {
            int value = transactionCounter.get().value();
            currentDatabase.put(value, databaseId);
        }
    }

    public static String currentDatabaseId() {
        TransactionCounter transactionCounter = ServiceContextHolder.transactionCounter.get();
        return currentDatabaseId.get().get(transactionCounter.value());
    }

    public static int increase() {
        return transactionCounter.get().increase();
    }

    public static int decrease() {
        return transactionCounter.get().decrease();
    }

    public static boolean hasTransaction() {
        return transactionCounter.get().value() != 0;
    }



    public static String getDatabaseId(Method method){
        DatasourceConfig.MethodMapping methodMapping = methodDatabaseMapping.get(method);
        if(methodMapping== null){
            return null;
        }
       return methodMapping.getDatabaseId();
    }


}
