package com.xie.java.aop;

import org.springframework.cglib.proxy.Enhancer;
import org.springframework.cglib.proxy.MethodInterceptor;
import org.springframework.cglib.proxy.MethodProxy;

import java.lang.reflect.Method;

/**
 * Created by xieyang on 18/3/2.
 */
public class EnhancerDemo {

    public static void main(String[] args) {
        Enhancer enhancer = new Enhancer();
        enhancer.setSuperclass(EnhancerDemo.class);
        enhancer.setCallback(new MethodInceptorImpl());
        EnhancerDemo demo = (EnhancerDemo) enhancer.create();
        demo.test();
        //System.out.println(demo);
    }

    public void test(){
        System.out.println(" EnhancerDemo test()");
    }

    public static class MethodInceptorImpl implements MethodInterceptor{

        public Object intercept(Object o, Method method, Object[] objects, MethodProxy methodProxy) throws Throwable {
            System.out.println("before invoke "+method.getName());
            Object invoke = methodProxy.invokeSuper(o, objects);
            System.out.println("after invoke "+method.getName());
            return invoke;
        }
    }

}
