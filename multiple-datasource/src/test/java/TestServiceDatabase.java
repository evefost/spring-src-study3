import com.xie.java.entity.User;
import com.xie.java.service.AService;
import com.xie.java.service.BService;
import com.xie.java.service.CService;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.support.ClassPathXmlApplicationContext;

/**
 * Created by xieyang on 19/7/4.
 */
public class TestServiceDatabase {
    public final Logger logger = LoggerFactory.getLogger(getClass());

    @Test
    public void testQuery(){
        String[]  configurLocations = {"spring-beans.xml"};
        ClassPathXmlApplicationContext context = new ClassPathXmlApplicationContext(configurLocations) ;
        AService aService = context.getBean(AService.class);
        BService bService = context.getBean(BService.class);
        CService cService = context.getBean(CService.class);

        User user = aService.queryById(2);
        User user1 = bService.queryById(2);
        User user2 = cService.queryById(2);


    }

    @Test
    public void testQueryWithTransaction() {
        String[] configurLocations = {"spring-beans.xml"};
        ClassPathXmlApplicationContext context = new ClassPathXmlApplicationContext(configurLocations);
        AService aService = context.getBean(AService.class);
        User user = aService.queryByIdWithTransaction(2);


        logger.debug("{}", user);
    }

    @Test
    public void testUpdate() {
        String[] configurLocations = {"spring-beans.xml"};
        ClassPathXmlApplicationContext context = new ClassPathXmlApplicationContext(configurLocations);
        AService aService = context.getBean(AService.class);
        User user = new User();
        user.setAge(111);
        user.setName("777777");
        aService.save(user);
        logger.debug("{}", user);
    }
}
