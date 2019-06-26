import com.xie.java.dao.UserMapper;
import com.xie.java.entity.User;
import com.xie.java.service.UserService;
import org.junit.Test;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import java.util.List;

/**
 * Created by xieyang on 18/3/3.
 */
public class UserServiceTest {

    @Test
    public void testAddUser(){
        String[]  configurLocations = {"spring-beans.xml"};
        ClassPathXmlApplicationContext context = new ClassPathXmlApplicationContext(configurLocations) ;
        UserService userService = context.getBean(UserService.class);
        User user = new User();
        user.setAge(111);
        user.setName("王小二");
        userService.save(user);
        System.out.println(user);
    }


    @Test
    public void testGetUser(){
        String[]  configurLocations = {"spring-beans.xml"};
        ClassPathXmlApplicationContext context = new ClassPathXmlApplicationContext(configurLocations);
        UserMapper userMapper = (UserMapper) context.getBean("userMapper");
        User user1 = userMapper.getUser(1);
        System.out.println(user1);
    }

    @Test
    public void testListUser(){
        String[]  configurLocations = {"spring-beans.xml"};
        ClassPathXmlApplicationContext context = new ClassPathXmlApplicationContext(configurLocations);
        UserService userService = context.getBean(UserService.class);
        List<User> users = userService.getUsers();
        System.out.println(users);
    }
}
