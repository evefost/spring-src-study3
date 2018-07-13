package com.xie.java.simple;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;

public class MyInvocaHandler implements InvocationHandler {
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
        try {
            System.out.println("调用前");
            for(Object arg:args){
                System.out.println("参数:"+arg);
            }
            System.out.println("做点事情返回");
            Class<?> returnType = method.getReturnType();
            if(void.class == returnType){
                return null;
            }
            return returnType.newInstance();
        } finally {
            System.out.println("调用后");
        }
    }
}
