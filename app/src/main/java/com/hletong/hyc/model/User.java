package com.hletong.hyc.model;

/**
 * Created by cc on 2016/10/14.
 */
public class User {
    private int age;
    private String name;

    public User(String name, int age) {
        this.name = name;
        this.age = age;
    }
    public User() {
    }
    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
