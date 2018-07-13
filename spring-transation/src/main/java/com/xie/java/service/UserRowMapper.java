package com.xie.java.service;

import com.xie.java.entity.User;
import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * Created by Administrator on 2018/3/2.
 */
public class UserRowMapper implements RowMapper<User>{


  public User mapRow(ResultSet rs, int rowNum) throws SQLException {
    User person = new User();
    person.setId(rs.getInt("id"));
    person.setName(rs.getString("name"));
    person.setAge(rs.getInt("age"));
    return person;
  }
}
