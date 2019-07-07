package com.xie.java.service;

import com.xie.java.entity.User;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * Created by Administrator on 2018/3/2.
 */
public interface BService {


    @Transactional
    void save(User user);
    User queryById(Integer id);

    List<User> getUsers();
}
