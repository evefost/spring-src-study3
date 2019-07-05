package com.xie.java.datasource;

import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

/**
 *
 * @author xieyang
 * @date 19/6/22
 */
public class DataSourcePropertiesParser {

    private Properties properties;

    private Map<String, DataSourceProperties> masterSlaverProperties;

    private Map<String, DataSourceProperties> datasourceProperties;

    private String defaultDatabaseId;

    public DataSourcePropertiesParser(Properties properties){
        this.properties = properties;
    }

    public void parse() {

        Map<String,String> masterKey = new HashMap<>();
        Map<String,String> slaverKey = new HashMap<>();
        String masterPrefix = "datasource.master";
        String slaverPrefix = "datasource.slaver";
        properties.forEach((key,value)->{
            if(key instanceof String && value instanceof String){
                String skey = (String) key;
                String svalue = (String) value;
                if(skey.startsWith(masterPrefix)&& skey.endsWith("id")){
                    masterKey.put(svalue,skey.substring(0,skey.length()-3));
                }else if(skey.startsWith(slaverPrefix) && skey.endsWith("id")){
                    slaverKey.put(svalue,skey.substring(0,skey.length()-3));
                }
            }
        });
        //主从分开属性解释
        Map<String, DataSourceProperties> masters = new HashMap<>(masterKey.size());
        processProperties(masterKey,masters);
        Map<String, DataSourceProperties> slavers = new HashMap<>(slaverKey.size());
        processProperties(slaverKey,slavers);
        //整合多套一主多从数据
        masters.forEach((mDataId,mProperites)->{
            slavers.forEach((sDataId,sProperites)->{
                if(mDataId.equals(sProperites.getParentId())){
                    mProperites.addSlaver(sProperites);
                }
            });
        });
        masterSlaverProperties = masters;
        datasourceProperties = masters;
        slavers.forEach((k, v) -> {
            datasourceProperties.put(k, v);
        });
        defaultDatabaseId = properties.getProperty("datasource.default.ds-id");
    }

    public Map<String, DataSourceProperties> getMasterSlaverProperties() {
        return masterSlaverProperties;
    }

    public Map<String, DataSourceProperties> getDatasourceProperties() {
        return datasourceProperties;
    }

    private void processProperties(Map<String, String> dataSourceKey, Map<String, DataSourceProperties> result) {
        for(Map.Entry<String,String> entry:dataSourceKey.entrySet()){
            DataSourceProperties dataSourceProperties = new DataSourceProperties();
            String dataPrefix = entry.getValue();
            String dataId = entry.getKey();
            dataSourceProperties.setId(dataId);
            properties.forEach((key, value)->{
                if(key instanceof String && value instanceof String){
                    String skey = (String) key;
                    String svalue = (String) value;
                    if(skey.startsWith(dataPrefix)&& skey.endsWith("url")){
                        dataSourceProperties.setUrl(svalue);
                    }else if(skey.startsWith(dataPrefix) && skey.endsWith("username")){
                        dataSourceProperties.setUsername(svalue);
                    }else if(skey.startsWith(dataPrefix) && skey.endsWith("password")){
                        dataSourceProperties.setPassword(svalue);
                    }else if(skey.startsWith(dataPrefix) && skey.endsWith("masterId")){
                        dataSourceProperties.setParentId(svalue);
                    }
                }
            });
            result.put(dataId,dataSourceProperties);
        };
    }

    public String getDefaultDatabaseId() {
        return defaultDatabaseId;
    }
}
