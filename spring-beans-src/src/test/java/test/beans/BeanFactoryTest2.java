package test.beans;

import com.xie.java.beans.CustomerBean;
import com.xie.java.beans.annotatio.Autowired;
import com.xie.java.beans.annotatio.Component;
import com.xie.java.beans.processor.ITarget;
import com.xie.java.beans.processor.MyPostProcessor;
import com.xie.java.beans.processor.Target;
import org.junit.Test;
import org.springframework.beans.MutablePropertyValues;
import org.springframework.beans.factory.config.BeanPostProcessor;
import org.springframework.beans.factory.config.RuntimeBeanReference;
import org.springframework.beans.factory.support.DefaultListableBeanFactory;
import org.springframework.beans.factory.support.GenericBeanDefinition;
import org.springframework.beans.factory.xml.XmlBeanDefinitionReader;
import org.springframework.core.io.ClassPathResource;

import java.lang.reflect.Field;
import java.util.List;

/**
 * 小实践
 */
public class BeanFactoryTest2 {




    /**
     * 1.手动添加beanDefinition到registry
     */
    @Test
    public void customerBeanDefinitions() {
        DefaultListableBeanFactory factory = new DefaultListableBeanFactory();
        ClassPathResource resource = new ClassPathResource("spring-beans.xml");
        XmlBeanDefinitionReader reader = new XmlBeanDefinitionReader(factory);
        reader.loadBeanDefinitions(resource);

        GenericBeanDefinition customerBeanDefinition = new GenericBeanDefinition();
        customerBeanDefinition.setBeanClass(CustomerBean.class);

        MutablePropertyValues propertyValues = new MutablePropertyValues();
        customerBeanDefinition.setPropertyValues(propertyValues);

        RuntimeBeanReference userRf = new RuntimeBeanReference("users");
        propertyValues.add("user",userRf);
        factory.registerBeanDefinition("customer",customerBeanDefinition);

        Object customer = factory.getBean("customer");
        System.out.println("xxx");

    }


    /**
     * 2.手动添加BeanPostProcessor
     */
    @Test
    public void customerPostProcessor() {
        DefaultListableBeanFactory factory = new DefaultListableBeanFactory();

        GenericBeanDefinition ps = new GenericBeanDefinition();
        ps.setBeanClass(MyPostProcessor.class);

        GenericBeanDefinition tg = new GenericBeanDefinition();
        tg.setBeanClass(Target.class);
        factory.registerBeanDefinition("processor",ps);
        factory.registerBeanDefinition("target",tg);
        Object processor = factory.getBean("processor");
        factory.addBeanPostProcessor((BeanPostProcessor) processor);
        ITarget target = (ITarget) factory.getBean("target");
        target.doSomething();


    }


    /**
     * 3.自定义注解，扫描被标记类并生成 beanDefinition 并把依赖注入
     */
    @Test
    public void customerAnnotation() {
        DefaultListableBeanFactory factory = new DefaultListableBeanFactory();

        List<Class<?>> classes = ClassScanUtil.getClasses("com.xie.java.beans.annotatio");
        GenericBeanDefinition db = null;
        for(Class c:classes){
            if(c.isAnnotationPresent(Component.class)){
                db = new GenericBeanDefinition();
                db.setBeanClass(c);
                MutablePropertyValues propertyValues = new MutablePropertyValues();
                db.setPropertyValues(propertyValues);
                Field[] declaredFields = c.getDeclaredFields();
                for(Field f:declaredFields){
                    if(f.isAnnotationPresent(Autowired.class)){
                        String name = f.getName();
                        RuntimeBeanReference userRf = new RuntimeBeanReference(name);
                        propertyValues.add(name,userRf);
                    }
                }
                factory.registerBeanDefinition(c.getSimpleName(),db);
            }

        }
        factory.preInstantiateSingletons();
        System.out.println("xxxx");

    }


}
