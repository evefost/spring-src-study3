package com.xie.java.withannotation;

import com.xie.java.annotation.Tag;
import com.xie.java.annotation.Topic;
import com.xie.java.annotation.VirtualApi;
import org.springframework.beans.factory.annotation.AnnotatedBeanDefinition;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.beans.factory.config.BeanDefinitionHolder;
import org.springframework.beans.factory.support.AbstractBeanDefinition;
import org.springframework.beans.factory.support.BeanDefinitionBuilder;
import org.springframework.beans.factory.support.BeanDefinitionReaderUtils;
import org.springframework.beans.factory.support.BeanDefinitionRegistry;
import org.springframework.context.annotation.ClassPathScanningCandidateComponentProvider;
import org.springframework.core.type.AnnotationMetadata;
import org.springframework.core.type.filter.AnnotationTypeFilter;

import java.lang.reflect.Method;
import java.util.*;

//@Component
public class AnnotationBeanRegistrar extends BeanRegistrar {


    private Map<String,List<MethodInfo>> producerMethods = new HashMap<String, List<MethodInfo>>();


    private Map<String,List<MethodInfo>> consumerMetods = new HashMap<String, List<MethodInfo>>();



    @Override
    protected void registerVirtualApi(BeanDefinitionRegistry registry,String name,
                                     AnnotationMetadata annotationMetadata) {
        String beanName = name;
        logger.info("创建的实例名:"+beanName);

        Map<String, Object> attritutes = annotationMetadata.getAnnotationAttributes(Topic.class.getCanonicalName());
        String className = annotationMetadata.getClassName();
        BeanDefinitionBuilder definition = BeanDefinitionBuilder
                .genericBeanDefinition(AnnotationFactoryBean.class);
        definition.addPropertyValue("name", name);
        definition.addPropertyValue("type", className);
        definition.addPropertyValue("topic", attritutes.get("value"));
        definition.addPropertyValue("producerMethods", producerMethods);
        definition.addPropertyValue("consumerMetods", consumerMetods);
        definition.setAutowireMode(AbstractBeanDefinition.AUTOWIRE_BY_TYPE);
        AbstractBeanDefinition beanDefinition = definition.getBeanDefinition();
        beanDefinition.setPrimary(false);
        BeanDefinitionHolder holder = new BeanDefinitionHolder(beanDefinition, beanName,
                new String[]{});
        BeanDefinitionReaderUtils.registerBeanDefinition(holder, registry);
    }



    public void scanImplTopicTags(Set<String> basePackages) throws ClassNotFoundException {
        //扫描指定的包，过滤出只打topic 及 tag标签的接口或类
        ClassPathScanningCandidateComponentProvider scanner = getScanner();
        scanner.setResourceLoader(this.resourceLoader);
        AnnotationTypeFilter inFilter = new AnnotationTypeFilter(
                Topic.class);
        AnnotationTypeFilter exFilter = new AnnotationTypeFilter(
                VirtualApi.class);
        scanner.addIncludeFilter(inFilter);
        scanner.addExcludeFilter(exFilter);


        for (String basePackage : basePackages) {
            Set<BeanDefinition> candidateComponents = scanner
                    .findCandidateComponents(basePackage);
            for (BeanDefinition candidateComponent : candidateComponents) {
                if (candidateComponent instanceof AnnotatedBeanDefinition) {
                    String beanClassName = candidateComponent.getBeanClassName();
                    Class<?> targetClass = classLoader.loadClass(beanClassName);
                    Topic topicAnnotation = targetClass.getAnnotation(Topic.class);
                    String topic = topicAnnotation.value();
                    Method[] methods = null;
                    if(targetClass.isInterface()){
                        methods = targetClass.getMethods();
                    }else {
                        methods = targetClass.getDeclaredMethods();
                    }

                    for(Method method:methods){
                        Tag annotation = method.getAnnotation(Tag.class);
                        if(annotation != null){
                            method.setAccessible(true);
                            String tag = annotation.value();
                            String key = topic+":"+tag;
                            MethodInfo methodInfo = new MethodInfo();
                            methodInfo.setTargetClass(targetClass);
                            methodInfo.setMethod(method);
                            List<MethodInfo> methodList = consumerMetods.get(key);
                            if(methodList == null){
                                methodList = new ArrayList<MethodInfo>();
                                consumerMetods.put(key,methodList);
                            }
                            methodList.add(methodInfo);
                        }
                    }

                }
            }
        }


    }
}
