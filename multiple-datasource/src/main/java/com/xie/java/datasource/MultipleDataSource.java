package com.xie.java.datasource;

import org.apache.commons.dbcp.BasicDataSource;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.context.EnvironmentAware;
import org.springframework.core.env.ConfigurableEnvironment;
import org.springframework.core.env.Environment;
import org.springframework.core.env.PropertiesPropertySource;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.core.io.support.PropertiesLoaderUtils;
import org.springframework.jdbc.datasource.AbstractDataSource;

import javax.sql.DataSource;
import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;

/**
 * @author xieyang
 * @date 19/6/21
 */
public class MultipleDataSource extends AbstractDataSource implements InitializingBean,EnvironmentAware {


    private Map<String, DataSource> dataSources;

    private Properties properties;

    private ConfigurableEnvironment environment;

    private String defaultDatabaseId;

    public String getDefaultDatabaseId() {
        return defaultDatabaseId;
    }


    @Override
    public void setEnvironment(Environment environment) {
        this.environment = (ConfigurableEnvironment) environment;

    }

    @Override
    public Connection getConnection() throws SQLException {
        String databaseId = ServiceContextHolder.currentDatabaseId();
        if (databaseId == null) {
            databaseId = defaultDatabaseId;
        }
        System.out.println("当前先择的数据库:" + databaseId);
        return dataSources.get(databaseId).getConnection();
    }

    @Override
    public Connection getConnection(String username, String password) throws SQLException {
        throw new UnsupportedOperationException("Not supported by MultipleDataSource");
    }

    public  static Map<String, DataSourceProperties> dataProperties;

    @Override
    public void afterPropertiesSet() throws Exception {

        doLoadloadProperties();
        DataSourcePropertiesParser dataSourcePropertiesParser = new DataSourcePropertiesParser(properties);
        dataProperties = dataSourcePropertiesParser.parse();
        defaultDatabaseId = properties.getProperty("datasource.default.ds-id");
        dataSources = new HashMap<>(dataProperties.size());
        for (Map.Entry<String, DataSourceProperties> entry : dataProperties.entrySet()) {
            String dataId = entry.getKey();
            DataSourceProperties value = entry.getValue();
            DataSource dataSource = createDataSource(value);
            if (value.getSlavers().isEmpty()) {
                dataSources.put(dataId, dataSource);
            } else {
                DynamicCompositDataSource masterDataSource = new DynamicCompositDataSource();
                masterDataSource.setMaster(dataSource);
                List<DataSourceProperties> slavers = value.getSlavers();
                for (DataSourceProperties slaverProperties : slavers) {
                    DataSource slaverDataSource = createDataSource(slaverProperties);
                    masterDataSource.addSlaver(slaverProperties.getId(), slaverDataSource);
                    dataSources.put(slaverProperties.getId(), slaverDataSource);
                }
                dataSources.put(dataId, masterDataSource);
            }

        }
    }

    private DataSource createDataSource(DataSourceProperties properties) {
        BasicDataSource dataSource = new BasicDataSource();
        dataSource.setDriverClassName("com.mysql.jdbc.Driver");
        dataSource.setUrl(properties.getUrl());
        dataSource.setUsername(properties.getUsername());
        dataSource.setPassword(properties.getPassword());
        return dataSource;

    }


    private void doLoadloadProperties() throws IOException {
        Resource path = new ClassPathResource("application.properties");
        properties = PropertiesLoaderUtils.loadProperties(path);
        PropertiesPropertySource mapPropertySource = new PropertiesPropertySource("myproperties", properties);
        ConfigurableEnvironment env = environment;
        env.getPropertySources().addLast(mapPropertySource);

    }


}
