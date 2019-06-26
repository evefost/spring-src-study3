package com.xie.java.dao;

import com.xie.java.datasource.annotation.DatabaseId;
import com.xie.java.entity.User;

import java.util.List;

/**
 * Created by xieyang on 18/3/3.
 */
@DatabaseId("ds1")
public interface UserMapper {

    @DatabaseId("ds0")
    void insertUser(User user);

    User getUser(Integer id);

    List<User> listUser();

}
