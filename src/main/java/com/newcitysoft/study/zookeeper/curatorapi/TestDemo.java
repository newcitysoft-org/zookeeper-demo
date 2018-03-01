package com.newcitysoft.study.zookeeper.curatorapi;

/**
 * @author lixin.tian@renren-inc.com
 * @date 2018/2/28 17:45
 */
public class TestDemo {
    public static void main(String[] args){
        ApiDemo.start();
        ApiDemo.checkExists("/node3");
    }
}
