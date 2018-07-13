import com.xie.java.entity.User;
import com.xie.java.service.UserService;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import java.util.List;

/**
 * Created by xieyang on 18/3/4.
 */
public class TransationTest {

    public static void main(String[] args) {

        ClassPathXmlApplicationContext applicationContext = new ClassPathXmlApplicationContext("spring-jdbc.xml");
        UserService userService = (UserService) applicationContext.getBean("userService");
        User user = new User();
        user.setName("张三2");
        user.setAge(123);
        userService.save(user);
        List<User> users = userService.getUsers();
        System.out.println(users);
    }
}
