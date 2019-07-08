package com.xie.java.datasource.interceptor;

import com.xie.java.datasource.RouteContextManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;

public  class ServiceInterceptor implements InvocationHandler {

        private final Logger logger = LoggerFactory.getLogger(getClass());


        private Object target;


        public ServiceInterceptor(Object target){
            this.target = target;
        }

        @Override
        public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
            if ("equals".equals(method.getName())) {
                return method.equals(target);
            } else if ("hashCode".equals(method.getName())) {
                return method.hashCode();
            } else if ("toString".equals(method.getName())) {
                return method.toString();
            }

            boolean transaction = false;
            int increase =  RouteContextManager.increase(transaction);
            if(increase == 1){
                logger.debug("进入service ");
            }
            String databaseId = RouteContextManager.getDatabaseId(method);
            String methodName = method.getDeclaringClass() + "." + method.getName();
            if(databaseId == null){
                databaseId = RouteContextManager.getDefaultDatabaseId();
                logger.debug("{} use default databaseId [{}]", methodName, databaseId);
            }else {
                logger.debug("{} bind databaseId [{}]", methodName, databaseId);
            }
            RouteContextManager.setCurrentDatabaseId(databaseId,transaction);
            try {
                return method.invoke(target,args);
            }finally {
                int decrease = RouteContextManager.decrease(transaction);
                RouteContextManager.setCurrentDatabaseId(null,transaction);
                if (decrease == 0) {
                    RouteContextManager.removeUpdateOperateFlag();
                    logger.debug("退出service");
                }
            }
        }
    }