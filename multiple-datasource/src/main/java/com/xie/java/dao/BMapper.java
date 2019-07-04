package com.xie.java.dao;

import com.xie.java.entity.User;

import java.util.List;

/**
 * Created by xieyang on 18/3/3.
 */
public interface BMapper {

    void insertUser(User user);

    User getUser(Integer id);

    List<User> listUser();

    User queryById(Integer id);

}
