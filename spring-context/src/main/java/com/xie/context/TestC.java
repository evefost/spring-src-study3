package com.xie.context;

import org.springframework.beans.factory.annotation.Autowired;

import javax.inject.Named;

/**
 * Created by Administrator on 2018/3/5.
 */
@Named
public class TestC {

  @Autowired
  TestB testB;

}
