package test;

import com.xie.java.aop.TestBean;
import org.springframework.beans.BeansException;
import org.springframework.context.support.ClassPathXmlApplicationContext;

/**
 * Created by Administrator on 2018/2/10.
 */
 //@Component
public class Test1 {


  public static void main(String[] args) throws BeansException {
    ClassPathXmlApplicationContext context = new ClassPathXmlApplicationContext("spring-aop.xml");
//    TestA testBean = (TestA) context.getBean("testA");
    TestBean testBean = (TestBean) context.getBean("testBean");
    testBean.test();
    System.out.println(testBean);
  }

}
