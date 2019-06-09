package com.xie.java.xsd.beans;

import org.springframework.beans.factory.annotation.Autowired;

/**
 * Created by xieyang on 17/3/19.
 */
public class School {

    private String name;

    private String address;

    private int students;

    @Autowired
    Student student;



    public School(String name,String address,int students){
        this.name = name;
        this.address = address;
        this.students = students;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public int getStudents() {
        return students;
    }

    public void setStudents(int students) {
        this.students = students;
    }
}
