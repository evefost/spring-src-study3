package test.beans;

import com.xie.java.beans.CustomerBean;
import com.xie.java.beans.annotatio.Autowired;
import com.xie.java.beans.annotatio.Component;
import com.xie.java.beans.processor.ITestProcessor;
import com.xie.java.beans.processor.Processor1;
import com.xie.java.beans.processor.TestProcessor;
import org.junit.Test;
import org.springframework.beans.MutablePropertyValues;
import org.springframework.beans.PropertyValue;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.beans.factory.config.BeanPostProcessor;
import org.springframework.beans.factory.config.RuntimeBeanReference;
import org.springframework.beans.factory.support.BeanDefinitionReader;
import org.springframework.beans.factory.support.BeanDefinitionRegistry;
import org.springframework.beans.factory.support.DefaultListableBeanFactory;
import org.springframework.beans.factory.support.GenericBeanDefinition;
import org.springframework.beans.factory.xml.XmlBeanDefinitionReader;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;

import java.lang.reflect.Field;
import java.util.List;

/**
 * Created by xieyang on 19/6/9.
 */
public class BeanFactoryTest {


    /**
     * 加载默认标签定议的beanDefinitions
     */
    @Test
    public void defaultXmlBeanDefinitions(){
        Resource classPathResource = new ClassPathResource("spring-beans.xml");
        BeanFactory factory = new DefaultListableBeanFactory();
        BeanDefinitionRegistry registry = (BeanDefinitionRegistry) factory;
        BeanDefinitionReader reader = new XmlBeanDefinitionReader(registry);
        reader.loadBeanDefinitions(classPathResource);
        Object testBean = factory.getBean("testBean");
        System.out.println("testBean="+testBean);
        Object users = factory.getBean("users");
        System.out.println("users="+users);

    }

    /**
     * 手动添加beanDefinition到registry
     */
    @Test
    public void customerBeanDefinitions() {
        Resource classPathResource = new ClassPathResource("spring-beans.xml");
        BeanFactory factory = new DefaultListableBeanFactory();
        BeanDefinitionRegistry registry = (BeanDefinitionRegistry) factory;
        BeanDefinitionReader reader = new XmlBeanDefinitionReader(registry);
        reader.loadBeanDefinitions(classPathResource);


        GenericBeanDefinition customer = new GenericBeanDefinition();
        customer.setBeanClass(CustomerBean.class);
        MutablePropertyValues propertyValues = new MutablePropertyValues();
        RuntimeBeanReference runtimeBeanReference = new RuntimeBeanReference("users");
        propertyValues.add("user", runtimeBeanReference);
        customer.setPropertyValues(propertyValues);

        registry.registerBeanDefinition("customer", customer);
        Object co = factory.getBean("customer");
        Object testBean = factory.getBean("testBean");
        System.out.println("ssss");

    }


    /**
     * 手动添加BeanPostProcessor
     */
    @Test
    public void customerPostProcessor() {
        Resource classPathResource = new ClassPathResource("spring-beans.xml");
        BeanFactory factory = new DefaultListableBeanFactory();
        BeanDefinitionRegistry registry = (BeanDefinitionRegistry) factory;
        BeanDefinitionReader reader = new XmlBeanDefinitionReader(registry);
        reader.loadBeanDefinitions(classPathResource);

        //1.创建Processor BeanDefinition
        GenericBeanDefinition beanDefinition = new GenericBeanDefinition();
        beanDefinition.setBeanClass(Processor1.class);
        registry.registerBeanDefinition("processor1", beanDefinition);
        ((DefaultListableBeanFactory) factory).addBeanPostProcessor((BeanPostProcessor) factory.getBean("processor1"));

        //添加目标实例
        GenericBeanDefinition targetDf = new GenericBeanDefinition();
        targetDf.setBeanClass(TestProcessor.class);
        registry.registerBeanDefinition("testTarget", targetDf);
        ITestProcessor bean = (ITestProcessor) factory.getBean("testTarget");
        bean.doSomething();
        System.out.println("ssss");

    }


    /**
     * 手动添加BeanPostProcessor
     */
    @Test
    public void customerAnnotation() {
        DefaultListableBeanFactory factory = new DefaultListableBeanFactory();
        BeanDefinitionRegistry registry = (BeanDefinitionRegistry) factory;


        List<Class<?>> classes = ClassScanUtil.getClasses("com.xie");

        GenericBeanDefinition genericBeanDefinition;
        for (Class c : classes) {


            if (c.isAnnotationPresent(Component.class)) {
                genericBeanDefinition = new GenericBeanDefinition();
                genericBeanDefinition.setBeanClass(c);
                Field[] fields = c.getDeclaredFields();
                MutablePropertyValues values = new MutablePropertyValues();
                genericBeanDefinition.setPropertyValues(values);
                for (Field f : fields) {
                    f.setAccessible(true);
                    PropertyValue value;
                    if (f.isAnnotationPresent(Autowired.class)) {
                        String beanName = f.getName();
                        RuntimeBeanReference runtimeBeanReference = new RuntimeBeanReference(beanName);
                        value = new PropertyValue(f.getName(), runtimeBeanReference);
                        values.add(f.getName(), runtimeBeanReference);
                    }
                }
                registry.registerBeanDefinition(c.getSimpleName(), genericBeanDefinition);
            }
        }
        factory.getBean("Techer");
        factory.preInstantiateSingletons();

        System.out.println("===");


    }


}
