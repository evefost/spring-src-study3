package com.xie.java.beans;

/**
 * 类说明
 * <p>
 *
 * @author 谢洋
 * @version 1.0.0
 * @date 2019/6/13
 */
public class CustomerBean {

    private String name = "xieyang";


    /**
     * 需要注入的对象
     */
    private User user;

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }
}
