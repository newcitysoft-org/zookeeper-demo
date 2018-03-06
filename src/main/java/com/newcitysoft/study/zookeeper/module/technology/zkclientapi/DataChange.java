package com.newcitysoft.study.zookeeper.module.technology.zkclientapi;

import com.newcitysoft.study.zookeeper.module.technology.zkclientapi.model.Gender;
import com.newcitysoft.study.zookeeper.module.technology.zkclientapi.model.Student;

/**
 * 负责在监听数据改变时，进行数据改变。
 * @author lixin.tian@renren-inc.com
 * @date 2018/2/28 16:09
 */
public class DataChange {
    public static void main(String[] args){
        Student student = new Student("lixin.tian2", "120402033", Gender.MAIL);

        ApiDemo.updateNodeData("/"+ApiDemo.serverId, student);
    }
}
