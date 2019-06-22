package com.xie.java.dao;

import com.xie.java.entity.User;

/**
 * Created by xieyang on 18/3/3.
 */
public interface UserMapper {

    void insertUser(User user);

    User getUser(Integer id);
}
