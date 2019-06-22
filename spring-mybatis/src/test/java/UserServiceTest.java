import com.xie.java.dao.UserMapper;
import com.xie.java.entity.User;
import org.springframework.context.support.ClassPathXmlApplicationContext;

/**
 * Created by xieyang on 18/3/3.
 */
public class UserServiceTest {

    public static void main(String[] args) {
        ClassPathXmlApplicationContext context = new ClassPathXmlApplicationContext("spring-beans.xml");
        UserMapper userMapper = (UserMapper) context.getBean("userMapper");
        User user = new User();
        user.setName("张三");
        user.setAge(12);
        //userMapper.insertUser(user);
        userMapper.getUser(1);
        System.out.println();
    }
}
