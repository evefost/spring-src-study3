package com.xie.java.datasource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.context.EnvironmentAware;
import org.springframework.core.env.ConfigurableEnvironment;
import org.springframework.core.env.Environment;
import org.springframework.jdbc.datasource.AbstractDataSource;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.Map;
import java.util.Properties;

/**
 * @author xieyang
 * @date 19/6/21
 */
public class MultipleDataSource extends AbstractDataSource implements InitializingBean,EnvironmentAware {
    public final Logger logger = LoggerFactory.getLogger(getClass());

    private Map<String, DataSource> dataSources;

    private MultipleSourceProperties sourceProperties;


    private Properties properties;

    private ConfigurableEnvironment environment;

    public void setDataSources(Map<String, DataSource> dataSources) {
        this.dataSources = dataSources;
    }

    public void setSourceProperties(MultipleSourceProperties sourceProperties) {
        this.sourceProperties = sourceProperties;
    }

    @Override
    public void setEnvironment(Environment environment) {
        this.environment = (ConfigurableEnvironment) environment;

    }

    public DataSource getDatasource(String datasourceId){
       return dataSources.get(datasourceId);
    }

    @Override
    public Connection getConnection() throws SQLException {
        String databaseId = RouteContextManager.currentDatabaseId();
        return dataSources.get(databaseId).getConnection();
    }

    @Override
    public Connection getConnection(String username, String password) throws SQLException {
        throw new UnsupportedOperationException("Not supported by MultipleDataSource");
    }



    @Override
    public void afterPropertiesSet() throws Exception {

    }


}
