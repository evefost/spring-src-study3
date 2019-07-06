/**
 *    Copyright 2009-2016 the original author or authors.
 *
 *    Licensed under the Apache License, Version 2.0 (the "License");
 *    you may not use this file except in compliance with the License.
 *    You may obtain a copy of the License at
 *
 *       http://www.apache.org/licenses/LICENSE-2.0
 *
 *    Unless required by applicable law or agreed to in writing, software
 *    distributed under the License is distributed on an "AS IS" BASIS,
 *    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *    See the License for the specific language governing permissions and
 *    limitations under the License.
 */
package com.xie.java.datasource;

import org.apache.ibatis.cursor.Cursor;
import org.apache.ibatis.executor.parameter.ParameterHandler;
import org.apache.ibatis.executor.statement.StatementHandler;
import org.apache.ibatis.mapping.BoundSql;
import org.apache.ibatis.mapping.MappedStatement;
import org.apache.ibatis.session.ResultHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.List;
import java.util.Map;

/**
 * @author Clinton Begin
 */
public class DynamicRoutingStatementHandler implements StatementHandler {
  private final Logger logger = LoggerFactory.getLogger(getClass());

  private DataSource dataSource;

  private Map<String, DataSourceProperties> datasourceProperties;

  private  StatementHandler delegate;

  public DynamicRoutingStatementHandler(StatementHandler delegate){
    this.delegate = delegate;
  }

  /**
   * 1.判断数据源是否一致
   * 2.一致不切换数据源
   * 3.不一致切换数据源
   * @param connection
   * @param transactionTimeout
   * @return
   * @throws SQLException
   */
  @Override
  public Statement prepare(Connection connection, Integer transactionTimeout) throws SQLException {

    String currentDatabaseId = RouteContextManager.currentDatabaseId();
    String url = connection.getMetaData().getURL();
    logger.info("改写数据源前:[{}][{}]",currentDatabaseId,url);
    MappedStatement mapStatement = RouteContextManager.getMapStatement();
    String bindDatabaseId = mapStatement.getDatabaseId();
    if(currentDatabaseId.equals(bindDatabaseId)){

    }else {
      logger.info("statement 绑定的数据源[{}]与当前源[{}]不一致，切换据源",bindDatabaseId,currentDatabaseId,url);
    }

    return delegate.prepare(connection, transactionTimeout);
  }

  @Override
  public void parameterize(Statement statement) throws SQLException {
    delegate.parameterize(statement);
  }

  @Override
  public void batch(Statement statement) throws SQLException {
    delegate.batch(statement);
  }

  @Override
  public int update(Statement statement) throws SQLException {
    return delegate.update(statement);
  }

  @Override
  public <E> List<E> query(Statement statement, ResultHandler resultHandler) throws SQLException {
    return delegate.<E>query(statement, resultHandler);
  }

  @Override
  public <E> Cursor<E> queryCursor(Statement statement) throws SQLException {
    return delegate.queryCursor(statement);
  }

  @Override
  public BoundSql getBoundSql() {
    return delegate.getBoundSql();
  }

  @Override
  public ParameterHandler getParameterHandler() {
    return delegate.getParameterHandler();
  }
}
