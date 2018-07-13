package com.xie.java.aop;

import org.aspectj.lang.annotation.After;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;

/**
 * Created by Administrator on 2018/2/27.
 */
@Aspect
public class AspectJTest {


  @Pointcut("execution(* *.test(..))")
  public void test(){

  }

  @Before("test()")
  public void beforeTest(){
    System.out.println("before test");
  }

  @After("test()")
  public void afterTest(){
    System.out.println("after test");
  }
}
