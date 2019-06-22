import com.xie.java.dao.UserMapper;
import com.xie.java.entity.User;
import org.junit.Test;
import org.springframework.context.support.ClassPathXmlApplicationContext;

/**
 * Created by xieyang on 18/3/3.
 */
public class UserServiceTest {




    @Test
    public void testGetUser(){
        String[]  configurLocations = {"spring-beans.xml"};
        ClassPathXmlApplicationContext context = new ClassPathXmlApplicationContext(configurLocations);
        UserMapper userMapper = (UserMapper) context.getBean("userMapper");
        User user1 = userMapper.getUser(1);
        System.out.println(user1);
    }
}
