import com.xie.java.entity.User;
import com.xie.java.service.AService;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.support.ClassPathXmlApplicationContext;

/**
 * Created by xieyang on 19/7/4.
 */
public class TestServiceSimple {
    public final Logger logger = LoggerFactory.getLogger(getClass());

    @Test
    public void testDefaultQuery(){
        String[]  configurLocations = {"spring-beans.xml"};
        ClassPathXmlApplicationContext context = new ClassPathXmlApplicationContext(configurLocations) ;
        AService aService = context.getBean(AService.class);
        User user = aService.queryById(2);

    }

    @Test
    public void testDefaultUpdate() {
        String[] configurLocations = {"spring-beans.xml"};
        ClassPathXmlApplicationContext context = new ClassPathXmlApplicationContext(configurLocations);
        AService aService = context.getBean(AService.class);
        User user = new User();
        user.setAge(111);
        user.setName("777777");
        aService.save(user);
        logger.debug("{}", user);
    }


    @Test
    public void testMutipleDaoQuery(){
        String[]  configurLocations = {"spring-beans.xml"};
        ClassPathXmlApplicationContext context = new ClassPathXmlApplicationContext(configurLocations) ;
        AService aService = context.getBean(AService.class);
        aService.queryByIdMutipleDao(2);

    }



    @Test
    public void testMutipleDaoUpdate(){
        String[]  configurLocations = {"spring-beans.xml"};
        ClassPathXmlApplicationContext context = new ClassPathXmlApplicationContext(configurLocations) ;
        AService aService = context.getBean(AService.class);
        User user = new User();
        user.setAge(111);
        user.setName("bbbbbbbb");
        aService.saveMutipleDao(user);

        System.out.printf("66666");

    }

    @Test
    public void testMutipleOperate() {
        String[] configurLocations = {"spring-beans.xml"};
        ClassPathXmlApplicationContext context = new ClassPathXmlApplicationContext(configurLocations);
        AService aService = context.getBean(AService.class);
        aService.mutipleOperate();
        aService.queryById(2);
    }


    @Test
    public void testMutipleOperate2() {
        String[] configurLocations = {"spring-beans.xml"};
        ClassPathXmlApplicationContext context = new ClassPathXmlApplicationContext(configurLocations);
        AService aService = context.getBean(AService.class);
        aService.mutipleOperate2();
    }


}
