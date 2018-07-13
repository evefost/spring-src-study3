package com.xie.java.mvc;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * Created by Administrator on 2018/3/9.
 */
@Controller
public class TestController {

  public  final Logger logger = LoggerFactory.getLogger(getClass());


  @GetMapping("getUser1")
  @ResponseBody
  public String getUser2(){
    logger.info("getUser1");
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
}
