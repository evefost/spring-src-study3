package com.xie.java.service;

import com.xie.java.dao.AMapper;
import com.xie.java.entity.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * Created by Administrator on 2018/3/2.
 */

@Service
public class AServiceImpl implements AService {

  @Autowired
  private AMapper userMapper;




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
  public User queryById(Integer id) {
    return userMapper.queryById(id);
  }


  @Override
  public List<User> getUsers() {
    return userMapper.listUser();
  }
}
