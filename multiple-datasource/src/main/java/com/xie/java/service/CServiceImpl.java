package com.xie.java.service;

import com.xie.java.dao.CMapper;
import com.xie.java.datasource.TransactionContextHolder;
import com.xie.java.datasource.annotation.DatabaseId;
import com.xie.java.entity.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * Created by Administrator on 2018/3/2.
 */

@Service
@DatabaseId("ds2")
public class CServiceImpl implements CService {

    @Autowired
    private CMapper userMapper;


    @Override
    @Transactional
    public void save(User user) {
        if (TransactionContextHolder.hasTransaction()) {
            System.out.println("BServeceImpl save有事务");
        } else {
            System.out.println("BServeceImpl save无事务");
        }

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
