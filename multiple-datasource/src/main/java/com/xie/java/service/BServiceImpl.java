package com.xie.java.service;

import com.xie.java.dao.BMapper;
import com.xie.java.datasource.RouteContextManager;
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
@DatabaseId("ds1")
public class BServiceImpl implements BService {
    private final Logger logger = LoggerFactory.getLogger(getClass());
    @Autowired
    private BMapper userMapper;


    @Override
    @Transactional(rollbackFor = Exception.class)
    public void save(User user) {
        if (RouteContextManager.hasTransaction()) {
            logger.info("BServeceImpl save有事务");
        } else {
            logger.info("BServeceImpl save无事务");
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
