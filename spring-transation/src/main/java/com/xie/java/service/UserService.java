package com.xie.java.service;

import com.xie.java.entity.User;

import java.util.List;

/**
 * Created by Administrator on 2018/3/2.
 */
public interface UserService {


  void save(User user);

  List<User> getUsers();
}
