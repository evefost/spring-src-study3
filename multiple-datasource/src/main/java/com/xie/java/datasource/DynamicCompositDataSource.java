package com.xie.java.datasource;

import org.springframework.jdbc.datasource.AbstractDataSource;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by xieyang on 19/6/23.
 */
public class DynamicCompositDataSource extends AbstractDataSource {

    private DataSource master;

    private List<DataSource> slavers = new ArrayList<>();

    public void setMaster(DataSource master){
        this.master = master;
    }

    public void addSlaver(DataSource slaver){
        this.slavers.add(slaver);
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
