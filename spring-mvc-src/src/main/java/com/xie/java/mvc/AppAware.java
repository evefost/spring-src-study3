package com.xie.java.mvc;

import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Component;

/**
 * Created by Administrator on 2018/3/10.
 */
@Component
public class AppAware implements ApplicationContextAware {


  /**\
   *
   * @param applicationContext
   * @throws BeansException
   */
  public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
    System.out.println(applicationContext);
    Object appUser = applicationContext.getBean("appUser");
    Object servletUser = applicationContext.getBean(  "servletUser");
    System.out.println(applicationContext);
  }
}
