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
import java.util.*;

/**
 * @author xieyang
 * @date 19/6/21
 */
public class MultipleDataSource extends AbstractDataSource implements InitializingBean,EnvironmentAware {


    private Map<String, DataSource> dataSources;

    private Properties properties;

    private ConfigurableEnvironment environment;


    @Override
    public void setEnvironment(Environment environment) {
        this.environment = (ConfigurableEnvironment) environment;

    }

    @Override
    public Connection getConnection() throws SQLException {
        Random random = new Random();
        int i = random.nextInt(3);
        return dataSources.get("ds"+i).getConnection();
    }

    @Override
    public Connection getConnection(String username, String password) throws SQLException {
        throw new UnsupportedOperationException("Not supported by MultipleDataSource");
    }



    @Override
    public void afterPropertiesSet() throws Exception {

        doLoadloadProperties();
        DataSourcePropertiesParser dataSourcePropertiesParser = new DataSourcePropertiesParser(properties);
        Map<String, DataSourceProperties> dataProperties = dataSourcePropertiesParser.parse();

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
                    masterDataSource.addSlaver(slaverDataSource);
                }
                dataSources.put(dataId, masterDataSource);
            }

        }

        System.out.printf("=====");

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
