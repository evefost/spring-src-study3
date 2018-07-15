package com.xie.java.withannotation;

import com.alibaba.fastjson.JSON;
import com.xie.java.annotation.Tag;
import com.xie.java.annotation.Topic;
import com.xie.java.annotation.VirtualApi;
import com.xie.java.simple.BeanRegistrar;
import org.springframework.beans.factory.InitializingBean;
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
import java.util.Map;
import java.util.Set;

//@Component
public class AnnotationBeanRegistrar extends BeanRegistrar  {

    private final VirtualPointInfo producerInfo = new VirtualPointInfo();

    private final VirtualPointInfo consumerInfo = new VirtualPointInfo();




    @Override

    public void registerVirtualApis(Set<String> basePackages,
                                    BeanDefinitionRegistry registry) throws ClassNotFoundException {
        super.registerVirtualApis(basePackages,registry);
        logger.info("当前应用 作为生产者 maybe publish those topics " + JSON.toJSONString(producerInfo.getTopics()));


    }

    @Override
    protected void registerVirtualApi(BeanDefinitionRegistry registry, String name,
                                      AnnotationMetadata annotationMetadata) throws ClassNotFoundException {
        String beanName = name;
        logger.info("即将创建的实例名:" + beanName);
        String beanClassName = annotationMetadata.getClassName();
        parseVirtualInfo(beanClassName,producerInfo);

        Map<String, Object> attritutes = annotationMetadata.getAnnotationAttributes(Topic.class.getCanonicalName());

        BeanDefinitionBuilder definition = BeanDefinitionBuilder
                .genericBeanDefinition(AnnotationFactoryBean.class);
        definition.addPropertyValue("name", name);
        definition.addPropertyValue("type", beanClassName);
        definition.addPropertyValue("topic", attritutes.get("value"));
        definition.addPropertyValue("producerInfo", producerInfo);
        definition.addPropertyValue("consumerInfo", consumerInfo);

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
                    parseVirtualInfo(beanClassName,consumerInfo);
                }
            }

        }
        logger.info("当前应用作为消费者 will subscribe those topics " + JSON.toJSONString(consumerInfo.getTopics()));


    }


    private void parseVirtualInfo(String beanClassName,VirtualPointInfo pointInfo) throws ClassNotFoundException {

        Class<?> targetClass = classLoader.loadClass(beanClassName);
        Topic topicAnnotation = targetClass.getAnnotation(Topic.class);
        String topic = topicAnnotation.value();
        Method[] methods = null;
        if (targetClass.isInterface()) {
            methods = targetClass.getMethods();
        } else {
            methods = targetClass.getDeclaredMethods();
        }

        for (Method method : methods) {
            Tag annotation = method.getAnnotation(Tag.class);
            if (annotation != null) {
                method.setAccessible(true);
                String tag = annotation.value();
                String key = topic + ":" + tag;
                MethodInfo methodInfo = new MethodInfo();
                methodInfo.setTargetClass(targetClass);
                methodInfo.setMethod(method);
                methodInfo.setTopic(topic);
                methodInfo.setTag(tag);
                MethodInfo rs = pointInfo.getMethodInfo(key);
                if (rs != null) {
                    String erromsg = rs.getTargetClass().getName() + " & " + targetClass.getName();
                    throw new RuntimeException("consumer  topic&&tag: " + key + " aready exist in " + erromsg);
                }
                pointInfo.putMethodInfo(key, methodInfo);
                pointInfo.getTopics().add(key);

            }
        }

    }

}
