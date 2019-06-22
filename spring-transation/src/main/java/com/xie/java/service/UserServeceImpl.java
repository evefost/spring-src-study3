package com.xie.java.service;

import com.xie.java.entity.User;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.transaction.annotation.Transactional;

import javax.sql.DataSource;
import java.sql.Types;
import java.util.List;
import java.util.Random;

/**
 * Created by Administrator on 2018/3/2.
 */

public class UserServeceImpl implements UserService {

  private JdbcTemplate jdbcTemplate;


  public void setDataSource(DataSource dataSource){
    this.jdbcTemplate = new JdbcTemplate(dataSource);
  }

  /**
   *
   * @param user
   */
  @Transactional
  public void save(User user) {
    jdbcTemplate.update("INSERT INTO `user`(name,age) VALUES (?,?)",
        new Object[]{user.getName(),user.getAge()},
        new int[]{Types.VARCHAR,Types.INTEGER});
    Random r = new Random();
    if(r.nextBoolean()){
      throw new RuntimeException("出错了...");
    }

  }


  public List<User> getUsers() {
    return jdbcTemplate.query("select * from user",new UserRowMapper());
  }
}
