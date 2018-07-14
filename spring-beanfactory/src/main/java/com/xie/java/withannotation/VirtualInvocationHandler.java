package com.xie.java.withannotation;

import com.alibaba.fastjson.JSON;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.context.ApplicationContext;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by xieyang on 18/7/14.
 */
public class VirtualInvocationHandler implements InvocationHandler {


    protected final Log logger = LogFactory.getLog(getClass());

    private AnnotationFactoryBean factoryBean;

    private ApplicationContext applicationContext;

    private Map<Method, TopicTag> topicTags = new HashMap<Method, TopicTag>();

    private Map<String, List<MethodInfo>> allTopicMethods;


    public VirtualInvocationHandler(ApplicationContext applicationContext, Map<String, List<MethodInfo>> allTopicMethods, Map<Method, TopicTag> topicTagMap) {
        this.applicationContext = applicationContext;
        this.allTopicMethods = allTopicMethods;
        topicTags = topicTagMap;

    }

    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {

        if ("equals".equals(method.getName())) {
            try {
                Object
                        otherHandler =
                        args.length > 0 && args[0] != null ? Proxy.getInvocationHandler(args[0]) : null;
                return equals(otherHandler);
            } catch (IllegalArgumentException e) {
                return false;
            }
        } else if ("hashCode".equals(method.getName())) {
            return method.hashCode();
        } else if ("toString".equals(method.getName())) {
            return method.toString();
        }
        try {
            logger.info("调用前....");
            logger.info("搞点事情再返回");
            TopicTag topicTag = topicTags.get(method);
            logger.info("输出 topic and tag: " + topicTag.toString());
            logger.info("参数:" + JSON.toJSONString(args));
            String key = topicTag.toString();
            List<MethodInfo> localMethods = allTopicMethods.get(key);
            if (localMethods != null) {
                for (MethodInfo methodInfo : localMethods) {
                    Object targetBean = applicationContext.getBean(methodInfo.getTargetClass());
                    Method targetMethod = methodInfo.getMethod();
                    targetMethod.invoke(targetBean, "ffffffff");
                }
            }

            Class<?> returnType = method.getReturnType();
            if (void.class == returnType) {
                return null;
            }
            return returnType.newInstance();
        } finally {
            logger.info("调用后....");
            logger.info("");
        }
    }
}
