package com.xie.java.datasource;

import java.io.IOException;
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

    public DataSourcePropertiesParser(Properties properties){
        this.properties = properties;
    }

    public Map<String,DataSourceProperties> parse() throws IOException {

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
        Map<String,DataSourceProperties> masters = new HashMap<>();
        processProperties(masterKey,masters);
        Map<String,DataSourceProperties> slavers = new HashMap<>();
        processProperties(slaverKey,slavers);
        //整合多套一主多从数据
        masters.forEach((mDataId,mProperites)->{
            slavers.forEach((sDataId,sProperites)->{
                if(mDataId.equals(sProperites.getParentId())){
                    mProperites.addSlaver(sProperites);
                }
            });
        });
       return masters;

    }


    private void processProperties(Map<String,String> dataSourceKey,Map<String,DataSourceProperties> result){
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

}
