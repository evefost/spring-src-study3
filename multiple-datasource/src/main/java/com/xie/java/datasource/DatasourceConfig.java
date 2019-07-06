package com.xie.java.datasource;

import com.xie.java.datasource.annotation.DatabaseId;
import com.xie.java.datasource.interceptor.PreTransactionInterceptor;
import org.apache.commons.dbcp.BasicDataSource;
import org.springframework.aop.framework.adapter.AdvisorAdapterRegistry;
import org.springframework.aop.framework.adapter.GlobalAdvisorAdapterRegistry;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.BeanClassLoaderAware;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.beans.factory.BeanFactoryAware;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.beans.factory.support.BeanDefinitionRegistry;
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
import org.springframework.stereotype.Component;

import javax.sql.DataSource;
import java.io.IOException;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;

/**
 * Created by xieyang on 19/7/4.
 */
@Component
@Order(Ordered.LOWEST_PRECEDENCE)
public class DatasourceConfig implements BeanFactoryAware, ResourceLoaderAware, BeanClassLoaderAware, InitializingBean, EnvironmentAware {

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
    }

    private void initDatasource() {
        Map<String, DataSource> dataSources = new HashMap<>(sourceProperties.getDatasourceProperties().size());
        for (Map.Entry<String, DataSourceProperties> entry : sourceProperties.getMasterSlaverProperties().entrySet()) {
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
        this.dataSource.setSourceProperties(sourceProperties);
        this.dataSource.setDataSources(dataSources);
    }

    private DataSource createDataSource(DataSourceProperties properties) {
        BasicDataSource dataSource = new BasicDataSource();
        dataSource.setDriverClassName("com.mysql.jdbc.Driver");
        dataSource.setUrl(properties.getUrl());
        dataSource.setUsername(properties.getUsername());
        dataSource.setPassword(properties.getPassword());
        return dataSource;

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
    }

    private void findDatabaseId() throws ClassNotFoundException {
        String[] beanDefinitionNames = registry.getBeanDefinitionNames();
        Map<Method, MethodMapping> methodMappingMap = new HashMap<>();
        for (String beanName : beanDefinitionNames) {
            BeanDefinition beanDefinition = registry.getBeanDefinition(beanName);
            Class<?> beanClass = classLoader.loadClass(beanDefinition.getBeanClassName());
            if (beanClass.isAnnotationPresent(DatabaseId.class)) {
                DatabaseId annotation = beanClass.getAnnotation(DatabaseId.class);
                parseMethodDatabaseId(annotation, beanClass, methodMappingMap);
                Class<?>[] interfaces = beanClass.getInterfaces();
                for (Class<?> ifc : interfaces) {
                    parseMethodDatabaseId(annotation, ifc, methodMappingMap);
                }
            } else {
                Class<?>[] interfaces = beanClass.getInterfaces();
                for (Class<?> ifc : interfaces) {
                    if (ifc.isAnnotationPresent(DatabaseId.class)) {
                        DatabaseId annotation = ifc.getAnnotation(DatabaseId.class);
                        parseMethodDatabaseId(annotation, ifc, methodMappingMap);
                    }

                }
            }
        }

        RouteContextManager.setMethodDatabaseMapping(methodMappingMap);
    }


    private void parseMethodDatabaseId(DatabaseId annotation, Class<?> clz, Map<Method, MethodMapping> methodMappingMap) {
        String databaseId = annotation.value();
        Method[] methods = clz.getDeclaredMethods();
        for (Method m : methods) {
            MethodMapping methodMapping = new MethodMapping();
            if (m.isAnnotationPresent(DatabaseId.class)) {
                methodMapping.setDatabaseId(m.getAnnotation(DatabaseId.class).value());
            }else {
                methodMapping.setDatabaseId(databaseId);
            }
            checkDatabaseExist(methodMapping.getDatabaseId(), clz, m);
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
