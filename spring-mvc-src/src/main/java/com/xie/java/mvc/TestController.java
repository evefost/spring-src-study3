package com.xie.java.mvc;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.context.support.XmlWebApplicationContext;

/**
 * Created by Administrator on 2018/3/9.
 */
@Controller
public class TestController implements ApplicationContextAware{

  public  final Logger logger = LoggerFactory.getLogger(getClass());


  @Value("${datasource.master}")
  private String master;

  @Autowired
  private XmlWebApplicationContext xmlWebApplicationContext;
  ApplicationContext applicationContext;

  @Autowired
  private Environment environment;

  @GetMapping("getUser1")
  @ResponseBody
  public String getUser2(){
    String property = environment.getProperty("datasource.master");
    logger.info("getUser1:"+property);
    return "xieyang";
  }

  @GetMapping("getUser")
  @ResponseBody
  public User getUser(String name) throws Exception {
    logger.info("te====================================="+name);
    User user = new User();
    user.setAge(123);
    user.setName("xieyang");
    return user;
  }

  @Override
  public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
    this.applicationContext = applicationContext;
  }
}
