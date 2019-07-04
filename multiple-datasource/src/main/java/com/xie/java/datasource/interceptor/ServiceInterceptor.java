package com.xie.java.datasource.interceptor;

import com.xie.java.datasource.ServiceContextHolder;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;

/**
 * Created by xieyang on 19/7/4.
 */
public class ServiceInterceptor implements InvocationHandler {

    private Object target;


    public ServiceInterceptor(Object target){
        this.target = target;
    }

    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
        if ("equals".equals(method.getName())) {
            try {
                Object
                        otherHandler =
                        args.length > 0 && args[0] != null ? Proxy.getInvocationHandler(args[0]) : null;
                return equals(otherHandler);
            } catch (IllegalArgumentException e) {
                return false;
            }
        } else if ("hashCode".equals(method.getName())) {
            return method.hashCode();
        } else if ("toString".equals(method.getName())) {
            return method.toString();
        }
        int increase = ServiceContextHolder.increase();
        if(increase == 1){
            System.out.println("进入service ");
        }
        String databaseId = ServiceContextHolder.getDatabaseId(method);
        ServiceContextHolder.setCurrentDatabaseId(databaseId);
        try {
            return method.invoke(target,args);
        }finally {
            int decrease = ServiceContextHolder.decrease();
            ServiceContextHolder.setCurrentDatabaseId(null);
            if (decrease == 0) {
                System.out.println("退出service");
            }
        }
    }
}
