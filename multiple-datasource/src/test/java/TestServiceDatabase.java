import com.xie.java.entity.User;
import com.xie.java.service.AService;
import com.xie.java.service.BService;
import com.xie.java.service.CService;
import org.junit.Test;
import org.springframework.context.support.ClassPathXmlApplicationContext;

/**
 * Created by xieyang on 19/7/4.
 */
public class TestServiceDatabase {


    @Test
    public void testQuery(){
        String[]  configurLocations = {"spring-beans.xml"};
        ClassPathXmlApplicationContext context = new ClassPathXmlApplicationContext(configurLocations) ;
        AService aService = context.getBean(AService.class);
        BService bService = context.getBean(BService.class);
        CService cService = context.getBean(CService.class);

        User user = aService.queryById(2);
//        User user1 = bService.queryById(2);
//        User user2 = cService.queryById(2);
        System.out.println(user);
//        System.out.println(user1);
//        System.out.println(user2);

    }
}
