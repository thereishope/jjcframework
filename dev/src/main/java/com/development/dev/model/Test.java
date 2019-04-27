package com.development.dev.model;

import java.io.Serializable;

/**
 * @author chenjiajun
 * @title Test
 * @project dev
 * @date 2019-03-01
 */
public class Test implements Serializable {

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
