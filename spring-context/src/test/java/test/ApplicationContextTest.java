package test;


import com.xie.context.TestA;
import org.junit.Test;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

/**
 * Created by Administrator on 2018/2/10.
 */
public class ApplicationContextTest {


    @Test
    public void mase(){
        ApplicationContext context = new ClassPathXmlApplicationContext("spring-beans.xml");
        TestA testBean = context.getBean(TestA.class);
        assert testBean != null;
    }


}
