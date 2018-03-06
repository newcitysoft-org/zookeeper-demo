package com.newcitysoft.study.zookeeper.module.technology.api;

import com.newcitysoft.study.zookeeper.util.Prop;
import com.newcitysoft.study.zookeeper.util.PropContainer;
import org.apache.zookeeper.WatchedEvent;
import org.apache.zookeeper.Watcher;
import org.apache.zookeeper.ZooKeeper;

import java.io.IOException;

/**
 * 与服务端创建会话
 * @author lixin.tian@renren-inc.com
 * @date 2018/2/28 11:04
 */
public class CreateSession implements Watcher{
    private static ZooKeeper zk;
    private String host;
    private int timeout;

    public CreateSession(String host, int timeout){
        this.host = host;
        this.timeout = timeout;
    }

    private void start(){
        try {
            zk = new ZooKeeper(host, timeout, this);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void stop(){
        try {
            zk.close();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void process(WatchedEvent event) {
        System.out.println("收到事件：" + event);
        if(event.getState() == Event.KeeperState.SyncConnected){
            doSomething();
        }
    }

    private static void doSomething() {
        System.out.println("do something!");
    }



    public static void main(String[] args){
        Prop prop = PropContainer.getProp("host.properties");

        CreateSession cs = new CreateSession(prop.get("localhost"), prop.getInt("timeout"));

        try {
            cs.start();
            System.out.println(zk.getState());
            Thread.sleep(5000);
            cs.stop();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
