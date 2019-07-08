package com.xie.java.datasource;

import com.xie.java.datasource.annotation.DataSource;
import com.xie.java.datasource.interceptor.PreTransactionInterceptor;
import org.apache.commons.dbcp.BasicDataSource;
import org.springframework.aop.framework.adapter.AdvisorAdapterRegistry;
import org.springframework.aop.framework.adapter.GlobalAdvisorAdapterRegistry;
import org.springframework.beans.BeansException;
import org.springframework.beans.MutablePropertyValues;
import org.springframework.beans.factory.BeanClassLoaderAware;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.beans.factory.BeanFactoryAware;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.beans.factory.support.BeanDefinitionRegistry;
import org.springframework.beans.factory.support.GenericBeanDefinition;
import org.springframework.context.EnvironmentAware;
import org.springframework.context.ResourceLoaderAware;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.core.env.ConfigurableEnvironment;
import org.springframework.core.env.Environment;
import org.springframework.core.env.PropertiesPropertySource;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;
import org.springframework.core.io.support.PropertiesLoaderUtils;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

/**
 * Created by xieyang on 19/7/4.
 */
@Component
@Order(Ordered.LOWEST_PRECEDENCE)
public class DatasourceConfig implements BeanFactoryAware, ResourceLoaderAware, BeanClassLoaderAware, InitializingBean, EnvironmentAware {

    public static  final String TRANSACTION_MANAGER_PREFIX = "transactionManager_";

    private BeanDefinitionRegistry registry;

    protected ResourceLoader resourceLoader;

    protected ClassLoader classLoader;


    private MultipleSourceProperties sourceProperties;

    private ConfigurableEnvironment environment;

    private MultipleDataSource dataSource;

    @Override
    public void setEnvironment(Environment environment) {
        this.environment = (ConfigurableEnvironment) environment;
        try {
            loadDatasourceProperties();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void setBeanFactory(BeanFactory beanFactory) throws BeansException {
        AdvisorAdapterRegistry instance = GlobalAdvisorAdapterRegistry.getInstance();
        if (beanFactory instanceof BeanDefinitionRegistry) {
            registry = (BeanDefinitionRegistry) beanFactory;
            String interceptorName = "org.springframework.transaction.interceptor.TransactionInterceptor#0";
            BeanDefinition interceptorBeanDf = registry.getBeanDefinition(interceptorName);
            interceptorBeanDf.setBeanClassName(PreTransactionInterceptor.class.getName());
        }
        sourceProperties = beanFactory.getBean(MultipleSourceProperties.class);
        dataSource = beanFactory.getBean(MultipleDataSource.class);
    }

    @Override
    public void setResourceLoader(ResourceLoader resourceLoader) {
        this.resourceLoader = resourceLoader;
    }


    @Override
    public void setBeanClassLoader(ClassLoader classLoader) {
        this.classLoader = classLoader;
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        findDatabaseId();
        initDatasource();
        registryPlatformTransactionManager();
    }

    private void initDatasource() {
        Map<String, javax.sql.DataSource> dataSources = new HashMap<>(sourceProperties.getDatasourceProperties().size());
        Map<String, DataSourceProperties> datasourceProperties = sourceProperties.getDatasourceProperties();
        for (Map.Entry<String, DataSourceProperties> entry : datasourceProperties.entrySet()) {
            String dataId = entry.getKey();
            DataSourceProperties value = entry.getValue();
            javax.sql.DataSource dataSource = createDataSource(value);
            dataSources.put(dataId, dataSource);
        }
        this.dataSource.setSourceProperties(sourceProperties);
        this.dataSource.setDataSources(dataSources);
    }

    private javax.sql.DataSource createDataSource(DataSourceProperties properties) {

        BasicDataSource dataSource = new BasicDataSource();
        dataSource.setDriverClassName("com.mysql.jdbc.Driver");
        dataSource.setUrl(properties.getUrl());
        dataSource.setUsername(properties.getUsername());
        dataSource.setPassword(properties.getPassword());
        DynamicDataSource dynamicDataSource = new DynamicDataSource();
        dynamicDataSource.setDelegate(dataSource);
        dynamicDataSource.setDatabaseId(properties.getId());
        dynamicDataSource.setUrl(properties.getUrl());
        dynamicDataSource.setMaster(properties.getParentId() == null);
        return dynamicDataSource;

    }

    private void loadDatasourceProperties() throws IOException {
        Resource path = new ClassPathResource("application.properties");
        Properties properties = PropertiesLoaderUtils.loadProperties(path);
        PropertiesPropertySource mapPropertySource = new PropertiesPropertySource("myproperties", properties);
        ConfigurableEnvironment env = environment;
        env.getPropertySources().addLast(mapPropertySource);
        DataSourcePropertiesParser dataSourcePropertiesParser = new DataSourcePropertiesParser(properties);
        dataSourcePropertiesParser.parse();
        sourceProperties.setDatasourceProperties(dataSourcePropertiesParser.getDatasourceProperties());
        sourceProperties.setMasterSlaverProperties(dataSourcePropertiesParser.getMasterSlaverProperties());
        sourceProperties.setDefaultDatabaseId(dataSourcePropertiesParser.getDefaultDatabaseId());
        RouteContextManager.setMultipleSourceProperties(sourceProperties);
    }

    private void findDatabaseId() throws ClassNotFoundException {
        String[] beanDefinitionNames = registry.getBeanDefinitionNames();
        Map<Method, MethodMapping> methodMappingMap = new HashMap<>();
        for (String beanName : beanDefinitionNames) {
            BeanDefinition beanDefinition = registry.getBeanDefinition(beanName);
            Class<?> beanClass = classLoader.loadClass(beanDefinition.getBeanClassName());
            if (beanClass.isAnnotationPresent(Service.class)) {
                parseMethodDatabaseId(beanClass, methodMappingMap);
                Class<?>[] interfaces = beanClass.getInterfaces();
                for (Class<?> ifc : interfaces) {
                    parseMethodDatabaseId(ifc, methodMappingMap);
                }
            } else {
                Class<?>[] interfaces = beanClass.getInterfaces();
                for (Class<?> ifc : interfaces) {
                    if (ifc.isAnnotationPresent(Service.class)) {
                        parseMethodDatabaseId(ifc, methodMappingMap);
                    }

                }
            }
        }

        RouteContextManager.setMethodDatabaseMapping(methodMappingMap);
    }


    private void registryPlatformTransactionManager() {

        Map<String, DataSourceProperties> datasourceProperties = sourceProperties.getDatasourceProperties();

        datasourceProperties.forEach((databaseId, properties) -> {
            if (properties.getParentId() == null) {
                GenericBeanDefinition beanDefinition = new GenericBeanDefinition();
                beanDefinition.setBeanClass(DataSourceTransactionManager.class);
                MutablePropertyValues propertyValues = new MutablePropertyValues();
                propertyValues.add("dataSource", dataSource.getDatasource(databaseId));
                beanDefinition.setPropertyValues(propertyValues);
                registry.registerBeanDefinition(TRANSACTION_MANAGER_PREFIX + databaseId, beanDefinition);
            }
        });

    }


    private void parseMethodDatabaseId(Class<?> clz, Map<Method, MethodMapping> methodMappingMap) {
        DataSource annotation = clz.getAnnotation(DataSource.class);
        String databaseId = null;
        if(annotation != null){
             databaseId = annotation.value();
        }

        Method[] methods = clz.getDeclaredMethods();
        for (Method m : methods) {
            MethodMapping methodMapping = new MethodMapping();
            if (m.isAnnotationPresent(DataSource.class)) {
                methodMapping.setDatabaseId(m.getAnnotation(DataSource.class).value());
            } else {
                methodMapping.setDatabaseId(databaseId);
            }
            if(methodMapping.getDatabaseId() != null){
                checkDatabaseExist(methodMapping.getDatabaseId(), clz, m);
            }
            methodMapping.setMethod(m);
            methodMappingMap.put(m, methodMapping);
        }
    }

    private void checkDatabaseExist(String databaseId, Class clz, Method method) {
        DataSourceProperties dataSourceProperties = sourceProperties.getDatasourceProperties().get(databaseId);
        if (dataSourceProperties == null) {
            throw new RuntimeException(clz.getName() + method.getName() + "bind 数据源:" + databaseId + "不存在");
        }
    }


    public static class MethodMapping {

        private Method method;

        private String databaseId;

        public Method getMethod() {
            return method;
        }

        public void setMethod(Method method) {
            this.method = method;
        }

        public String getDatabaseId() {
            return databaseId;
        }

        public void setDatabaseId(String databaseId) {
            this.databaseId = databaseId;
        }
    }
}
