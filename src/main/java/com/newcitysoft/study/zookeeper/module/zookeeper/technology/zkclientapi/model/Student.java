package com.newcitysoft.study.zookeeper.module.zookeeper.technology.zkclientapi.model;

import java.io.Serializable;

/**
 * @author lixin.tian@renren-inc.com
 * @date 2018/2/28 14:48
 */
public class Student implements Serializable{
    private static final long serialVersionUID = 1L;

    private String name;
    private String id;
    private Gender gender;

    public Student(String name, String id, Gender gender) {
        this.name = name;
        this.id = id;
        this.gender = gender;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public Gender getGender() {
        return gender;
    }

    public void setGender(Gender gender) {
        this.gender = gender;
    }

    @Override
    public String toString() {
        return "Student{" +
                "name='" + name + '\'' +
                ", id='" + id + '\'' +
                ", gender=" + gender +
                '}';
    }
}
