package com.xie.java.jdbc;

import java.sql.Types;
import java.util.List;
import javax.sql.DataSource;
import org.springframework.jdbc.core.JdbcTemplate;

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
  @Override
  public void save(User user) {
    jdbcTemplate.update("INSERT INTO `user`(name,age) VALUES (?,?)",
        new Object[]{user.getName(),user.getAge()},
        new int[]{Types.VARCHAR,Types.INTEGER});
  }

  @Override
  public List<User> getUsers() {
    return jdbcTemplate.query("select * from user",new UserRowMapper());
  }
}
