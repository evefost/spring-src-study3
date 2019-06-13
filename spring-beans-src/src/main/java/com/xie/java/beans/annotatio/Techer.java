package com.xie.java.beans.annotatio;

/**
 * 类说明
 * <p>
 *
 * @author 谢洋
 * @version 1.0.0
 * @date 2019/6/13
 */
@Component
public class Techer {


    @Autowired
    private Student Student;

    public com.xie.java.beans.annotatio.Student getStudent() {
        return Student;
    }

    public void setStudent(com.xie.java.beans.annotatio.Student student) {
        Student = student;
    }
}
