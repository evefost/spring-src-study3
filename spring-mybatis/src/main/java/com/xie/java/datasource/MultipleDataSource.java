package com.xie.java.datasource;

import org.apache.commons.dbcp.BasicDataSource;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.jdbc.datasource.AbstractDataSource;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;
import java.util.Set;

/**
 * Created by xieyang on 19/6/21.
 */
public class MultipleDataSource extends AbstractDataSource implements InitializingBean {

    private Map<String,String> masterUrls;

    private Map<String,String> slaserUrls;

    private Map<String,DataSource> dataSources;

    @Override
    public Connection getConnection() throws SQLException {
        Random random = new Random();
       int i = random.nextInt(3)+1;
        return dataSources.get(i+"").getConnection();
    }

    @Override
    public Connection getConnection(String username, String password) throws SQLException {
        throw new UnsupportedOperationException("Not supported by MultipleDataSource");
    }

    public Map<String, String> getMasterUrls() {
        return masterUrls;
    }

    public void setMasterUrls(Map<String, String> masterUrls) {
        this.masterUrls = masterUrls;
    }

    public Map<String, String> getSlaserUrls() {
        return slaserUrls;
    }

    public void setSlaserUrls(Map<String, String> slaserUrls) {
        this.slaserUrls = slaserUrls;
    }



    @Override
    public void afterPropertiesSet() throws Exception {
        BasicDataSource basicDataSource = null;
        dataSources = new HashMap<>(masterUrls.size());
        int i=0;
        Set<Map.Entry<String, String>> entries = masterUrls.entrySet();
        for(Map.Entry<String,String> entry:entries){
            i++;
            basicDataSource = new BasicDataSource();
            basicDataSource.setUrl(entry.getValue());
            basicDataSource.setUsername("root");
            basicDataSource.setPassword("root");
            basicDataSource.setDriverClassName("com.mysql.jdbc.Driver");
            dataSources.put(""+i,basicDataSource);
        }
    }
}
