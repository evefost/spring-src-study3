package com.xie.java.service;

import com.xie.java.dao.UserMapper;
import com.xie.java.entity.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * Created by Administrator on 2018/3/2.
 */

@Service
public class UserServeceImpl implements UserService {

  @Autowired
  private UserMapper userMapper;


  /**
   *
   * @param user
   */
  @Override
  @Transactional
  public void save(User user) {

    userMapper.insertUser(user);
  }


  @Override
  public List<User> getUsers() {
    return userMapper.listUser();
  }
}
