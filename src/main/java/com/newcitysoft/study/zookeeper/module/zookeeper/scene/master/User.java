package com.newcitysoft.study.zookeeper.module.zookeeper.scene.master;

import java.io.Serializable;

/**
 * @author lixin.tian@renren-inc.com
 * @date 2018/3/1 11:26
 */
public class User implements Serializable{

    private static final long serialVersionUID = 3377862741619431295L;
    private String name;
    private String password;

    public User() {
    }

    public User(String name, String password) {
        this.name = name;
        this.password = password;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    @Override
    public String toString() {
        return "User{" +
                "name='" + name + '\'' +
                ", password='" + password + '\'' +
                '}';
    }
}

