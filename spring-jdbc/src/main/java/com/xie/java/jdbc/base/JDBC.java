package com.xie.java.jdbc.base;

import com.xie.java.jdbc.User;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class JDBC {

  Connection conn;
  PreparedStatement ps;
  ResultSet rs;

  /**
   * 写一个连接数据库的方法
   */
  public Connection getConnection() {
    String url = "jdbc:mysql://localhost:3306/demo";
    String userName = "root";
    String password = "root";
    try {
      Class.forName("com.mysql.jdbc.Driver");
    } catch (ClassNotFoundException e) {
      // TODO Auto-generated catch block
      System.out.println("找不到驱动！");
      e.printStackTrace();
    }
    try {
      conn = DriverManager.getConnection(url, userName, password);
      if (conn != null) {
        System.out.println("connection successful");
      }
    } catch (SQLException e) {
      // TODO Auto-generated catch block
      System.out.println("connection fail");
      e.printStackTrace();
    }
    return conn;
  }

  /**
   * 写一个查询数据库语句的方法
   */
  public void querySql() {
    //1、执行静态SQL语句。通常通过Statement实例实现。
    // 2、执行动态SQL语句。通常通过PreparedStatement实例实现。
    // 3、执行数据库存储过程。通常通过CallableStatement实例实现。
    System.out.println("query");
    User u;
    String sql = "select * from user";
    try {
      conn = getConnection();
      ps = conn.prepareStatement(sql);
      rs = ps.executeQuery(sql);
      // 4.处理结果集
      while (rs.next()) {
        u = new User();
        u.setId(rs.getInt("id"));
        u.setName(rs.getString("name"));
        u.setAge(rs.getInt("age"));
        System.out.println(u.getName());
      }
      System.out.println(rs.next());
    } catch (SQLException e) {
      // TODO Auto-generated catch block
      e.printStackTrace();
    } finally {
      //释放资源
      try {
        rs.close();
        ps.close();
        conn.close();
      } catch (SQLException e) {
        // TODO Auto-generated catch block
        e.printStackTrace();
      }
    }
  }

  public int add(User user) {
    User u = new User();
    u.setName("李四");
    int row = 0;
    String sql = "insert into user(name,age) values(?,?)";
    try {
      conn = getConnection();
      ps = conn.prepareStatement(sql);
      ps.setString(1, user.getName());
      ps.setInt(2, user.getAge());
      row = ps.executeUpdate();
    } catch (SQLException e) {
      e.printStackTrace();
    } finally {
      try {
        ps.close();
        conn.close();
      } catch (SQLException e) {
        // TODO Auto-generated catch block
        e.printStackTrace();
      }

    }
    return row;
  }

  /**
   * @return修改数据
   */
  public int update(User user) {
    int row = 0;
    String sql = "update user set name=?,age=? where id=?";
    try {
      conn = getConnection();//连接数据库
      ps = conn.prepareStatement(sql);
      ps.setInt(3, user.getId());
      ps.setString(1, user.getName());
      ps.setInt(2, user.getAge());
      row = ps.executeUpdate();
      System.out.println(row);
    } catch (SQLException e) {
      e.printStackTrace();
    } finally {
      //释放资源
      try {
        ps.close();
        conn.close();
      } catch (SQLException e) {
        // TODO Auto-generated catch block
        e.printStackTrace();
      }
    }
    return row;
  }

  /**
   * @return删除操作
   */
  public int delete(User user) {
    User u;
    int row = 0;
    String sql = "delete from user where id=?";
    try {
      conn = getConnection();//连接数据库
      ps = conn.prepareStatement(sql);// 2.创建Satement并设置参数
      ps.setInt(1, user.getId());
      // 4.处理结果集
      row = ps.executeUpdate();
      System.out.println(row);
    } catch (SQLException e) {
      // TODO Auto-generated catch block
      e.printStackTrace();
    } finally {
      //释放资源【執行完sql要記得釋放資源】
      try {
        ps.close();
        conn.close();
      } catch (SQLException e) {
        // TODO Auto-generated catch block
        e.printStackTrace();
      }
    }
    return row;
  }

  public static void main(String[] args) {
    JDBC j = new JDBC();
    User u = new User();
    u.setId(4);
    u.setAge(16);
    u.setName("666666");
    j.querySql();
    //j.update(u);
    j.add(u);
  }
}