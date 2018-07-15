package com.xie.java.withannotation;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by xieyang on 18/7/15.
 */
public class VirtualPointInfo {

    private final Map<String,MethodInfo> methodInfoMap = new HashMap<String, MethodInfo>();

    private final List<String> topics = new ArrayList<String>();

    public Map<String, MethodInfo> getMethodInfoMap() {
        return methodInfoMap;
    }


    public MethodInfo getMethodInfo(String key) {
        return methodInfoMap.get(key);
    }

    public MethodInfo putMethodInfo(String key,MethodInfo methodInfo) {
        return methodInfoMap.put(key,methodInfo);
    }



    public List<String> getTopics() {
        return topics;
    }


}
