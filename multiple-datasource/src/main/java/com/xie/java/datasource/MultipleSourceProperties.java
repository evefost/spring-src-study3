package com.xie.java.datasource;

import java.util.Map;

/**
 * 类说明
 * <p>
 *
 * @author 谢洋
 * @version 1.0.0
 * @date 2019/7/5
 */

public class MultipleSourceProperties {

    private String defaultDatabaseId;

    private Map<String, DataSourceProperties> masterSlaverProperties;

    private Map<String, DataSourceProperties> datasourceProperties;

    public Map<String, DataSourceProperties> getMasterSlaverProperties() {
        return masterSlaverProperties;
    }

    public void setMasterSlaverProperties(Map<String, DataSourceProperties> masterSlaverProperties) {
        this.masterSlaverProperties = masterSlaverProperties;
    }

    public Map<String, DataSourceProperties> getDatasourceProperties() {
        return datasourceProperties;
    }

    public void setDatasourceProperties(Map<String, DataSourceProperties> datasourceProperties) {
        this.datasourceProperties = datasourceProperties;
    }


    public DataSourceProperties getProperties(String databaseId) {
        return datasourceProperties.get(databaseId);
    }

    public String getDefaultDatabaseId() {
        return defaultDatabaseId;
    }

    public void setDefaultDatabaseId(String defaultDatabaseId) {
        this.defaultDatabaseId = defaultDatabaseId;
    }
}
