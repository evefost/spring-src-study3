package com.xie.context;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * Created by Administrator on 2018/3/5.
 */
@Component("testA")
public class TestA {

  @Autowired
  TestB testB;

}
