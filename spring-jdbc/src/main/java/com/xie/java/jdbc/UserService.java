package com.xie.java.jdbc;

import java.util.List;

/**
 * Created by Administrator on 2018/3/2.
 */
public interface UserService {


  void save(User user);

  List<User> getUsers();
}
