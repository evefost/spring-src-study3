package test;


import com.xie.context.TestA;
import org.springframework.context.support.ClassPathXmlApplicationContext;

/**
 * Created by Administrator on 2018/2/10.
 */
//@Component
public class Test1 {


    public static void main(String[] args) {
        ClassPathXmlApplicationContext context = new ClassPathXmlApplicationContext("spring-beans.xml");
        TestA testBean = context.getBean(TestA.class);
        System.out.println(testBean);
    }


}
