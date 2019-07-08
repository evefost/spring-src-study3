package com.xie.java.datasource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.jdbc.datasource.AbstractDataSource;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.SQLException;

/**
 * Created by xieyang on 19/6/23.
 */
public class DynamicDataSource extends AbstractDataSource {

    public final Logger logger = LoggerFactory.getLogger(getClass());

    private boolean isMaster;

    private String url;

    private String databaseId;

    private DataSource delegate;

    public void setMaster(boolean master) {
        isMaster = master;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public void setDatabaseId(String databaseId) {
        this.databaseId = databaseId;
    }

    public void setDelegate(DataSource delegate) {
        this.delegate = delegate;
    }

    @Override
    public Connection getConnection() throws SQLException {
        boolean trans = RouteContextManager.hasTransaction();
        logger.debug("连接[{}][{}][{}]({})", isMaster ? "主库" : "从库", databaseId, url, trans ? "有事务" : "无事务");
        return delegate.getConnection();
    }

    @Override
    public Connection getConnection(String s, String s1) throws SQLException {
        throw new UnsupportedOperationException("Not supported by MultipleDataSource");
    }
}
