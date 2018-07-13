import com.xie.java.jdbc.User;
import com.xie.java.jdbc.UserService;
import java.util.List;
import org.springframework.context.support.ClassPathXmlApplicationContext;

/**
 * Created by Administrator on 2018/3/2.
 */
public class TestJdbc {

  public static void main(String[] args) {

    ClassPathXmlApplicationContext applicationContext = new ClassPathXmlApplicationContext("spring-jdbc.xml");
    UserService userService = (UserService) applicationContext.getBean("userService");
    User user = new User();
    user.setName("张三");
    user.setAge(12);
    userService.save(user);
    List<User> users = userService.getUsers();
    System.out.println(users);
  }
}
