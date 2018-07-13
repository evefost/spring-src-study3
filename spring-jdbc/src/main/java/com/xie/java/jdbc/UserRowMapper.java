package com.xie.java.jdbc;

import java.sql.ResultSet;
import java.sql.SQLException;
import org.springframework.jdbc.core.RowMapper;

/**
 * Created by Administrator on 2018/3/2.
 */
public class UserRowMapper implements RowMapper<User>{

  @Override
  public User mapRow(ResultSet rs, int rowNum) throws SQLException {
    User person = new User();
    person.setId(rs.getInt("id"));
    person.setName(rs.getString("name"));
    person.setAge(rs.getInt("age"));
    return person;
  }
}
