package com.xie.java.datasource;

import org.springframework.jdbc.datasource.AbstractDataSource;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by xieyang on 19/6/23.
 */
public class DynamicCompositDataSource extends AbstractDataSource {

    private String databaseId;

    private DataSource master;



    private Map<String,DataSource> slavers = new HashMap<>();

    public void setMaster(DataSource master){
        this.master = master;
    }

    public void addSlaver(String databaseId,DataSource slaver){
        this.slavers.put(databaseId,slaver);
    }

    public String getDatabaseId() {
        return databaseId;
    }

    public void setDatabaseId(String databaseId) {
        this.databaseId = databaseId;
    }

    @Override
    public Connection getConnection() throws SQLException {
        return master.getConnection();
    }

    @Override
    public Connection getConnection(String username, String password) throws SQLException {
        throw new UnsupportedOperationException("Not supported by MultipleDataSource");
    }
}
