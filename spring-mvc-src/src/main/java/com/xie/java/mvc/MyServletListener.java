package com.xie.java.mvc;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;

/**
 * Created by xieyang on 18/3/8.
 */
public class MyServletListener implements ServletContextListener{
    public void contextInitialized(ServletContextEvent sce) {
        System.out.println("contextInitialized.....");
    }

    public void contextDestroyed(ServletContextEvent sce) {
        System.out.println("contextDestroyed....");
    }
}
