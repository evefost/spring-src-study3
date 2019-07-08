package com.xie.java.service;

import com.xie.java.datasource.annotation.DataSource;
import com.xie.java.entity.User;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * Created by Administrator on 2018/3/2.
 */
@DataSource("ds0_1")
public interface AService {

//  @Transactional
  User queryById(Integer id);

  User queryByIdWithTransaction(Integer id);

  @Transactional
  void save(User user);

  void saveWithTransaction(User user);

  List<User> getUsers();




  void queryByIdMutipleDao(Integer id);


  @Transactional
  void saveMutipleDao(User user);


  @Transactional
  void mutipleOperate();


    @Transactional
  void mutipleOperate2();
}
