package com.xie.java.service;

import com.xie.java.datasource.annotation.DatabaseId;
import com.xie.java.entity.User;

import java.util.List;

/**
 * Created by Administrator on 2018/3/2.
 */
@DatabaseId("ds0")
public interface AService {


  void save(User user);

  User queryById(Integer id);

  List<User> getUsers();
}
