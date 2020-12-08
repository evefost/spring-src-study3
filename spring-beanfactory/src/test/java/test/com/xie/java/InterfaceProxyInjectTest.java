package test.com.xie.java;

import com.xie.java.InterFaceA;
import com.xie.java.beans.User;
import com.xie.java.withannotation.AnnotationBeanRegistrar;
import com.xie.java.withannotation.AnnotationFactoryBean;
import org.junit.Test;
import org.springframework.beans.factory.support.BeanDefinitionRegistry;
import org.springframework.beans.factory.support.GenericBeanDefinition;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import java.util.HashSet;
import java.util.Set;

/**
 * 模拟一个接品，通过mq发送主题及top
 */
public class InterfaceProxyInjectTest {


    @Test
    public  void testProxyInject() throws ClassNotFoundException {
        ClassPathXmlApplicationContext context = new ClassPathXmlApplicationContext("spring-beans.xml");
        context.refresh();
        BeanDefinitionRegistry registry = (BeanDefinitionRegistry) context.getBeanFactory();
        //BeanRegistrar 先注册到容器内
        GenericBeanDefinition genericBeanDefinition = new GenericBeanDefinition();
        genericBeanDefinition.setBeanClass(AnnotationBeanRegistrar.class);

        registry.registerBeanDefinition("myBeanRegistrar", genericBeanDefinition);

        AnnotationBeanRegistrar beanRegistrar = context.getBean(AnnotationBeanRegistrar.class);
        //指定扫描包的路径
        Set<String> packages = new HashSet<String>();
        packages.add("com.xie.java");
        beanRegistrar.registerVirtualApis(packages, registry);
        beanRegistrar.scanImplTopicTags(packages);

        InterFaceA faceA = context.getBean(InterFaceA.class);
        faceA.testA("这是调用a 方法的参数");
        User user = new User();
        user.setName("xieyang");
        user.setAge(28);
        faceA.addUser(user);
        AnnotationFactoryBean factoryBeanA = (AnnotationFactoryBean) context.getBean("&Proxy$com.xie.java.InterFaceA");
        AnnotationFactoryBean factoryBeanB = (AnnotationFactoryBean) context.getBean("&Proxy$com.xie.java.InterfaceB");
        assert  true;
    }
}
