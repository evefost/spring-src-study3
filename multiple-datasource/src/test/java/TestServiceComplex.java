import com.xie.java.entity.User;
import com.xie.java.service.AService;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.support.ClassPathXmlApplicationContext;

/**
 * Created by xieyang on 19/7/4.
 */
public class TestServiceComplex {
    public final Logger logger = LoggerFactory.getLogger(getClass());

    @Test
    public void testDefaultQuery(){
        String[]  configurLocations = {"spring-beans.xml"};
        ClassPathXmlApplicationContext context = new ClassPathXmlApplicationContext(configurLocations) ;
        AService aService = context.getBean(AService.class);
        User user = aService.queryById(2);
        System.out.println("xxxxxx");
    }



}