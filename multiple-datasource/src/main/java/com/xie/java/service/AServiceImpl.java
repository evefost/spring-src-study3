package com.xie.java.service;

import com.xie.java.dao.AMapper;
import com.xie.java.datasource.annotation.DatabaseId;
import com.xie.java.entity.User;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * Created by Administrator on 2018/3/2.
 */

@Service
public class AServiceImpl implements AService {
  private final Logger logger = LoggerFactory.getLogger(getClass());
  @Autowired
  private AMapper userMapper;


  /**
   *
   * @param user
   */
  @Override

  @DatabaseId("s_ds0")
  public void save(User user) {
    userMapper.insertUser(user);
  }

  @Override
  @Transactional(rollbackFor = Exception.class)
  public void saveWithTransaction(User user) {
    userMapper.insertUser(user);
  }

  @Override
  public User queryById(Integer id) {
    return userMapper.queryById(id);
  }

  @Override
  @Transactional(rollbackFor = Exception.class)
  public User queryByIdWithTransaction(Integer id) {
    return userMapper.queryById(id);
  }


  @Override
  public List<User> getUsers() {
    return userMapper.listUser();
  }
}
