package com.xie.java.entity;

import com.xie.java.datasource.annotation.DataSourceId;

/**
 * Created by xieyang on 18/3/3.
 */
@DataSourceId(id="")
public class User {

    private String name;
    private int age;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }
}
