package com.newcitysoft.study.zookeeper.module.technology.api;

import com.newcitysoft.study.zookeeper.util.Prop;
import com.newcitysoft.study.zookeeper.util.PropContainer;
import org.apache.zookeeper.CreateMode;
import org.apache.zookeeper.KeeperException;
import org.apache.zookeeper.WatchedEvent;
import org.apache.zookeeper.Watcher;
import org.apache.zookeeper.ZooDefs;
import org.apache.zookeeper.ZooKeeper;

import java.io.IOException;

/**
 * 同步创建znode
 * @author lixin.tian@renren-inc.com
 * @date 2018/2/28 11:26
 */
public class CreateNodeSync implements Watcher{
    private static ZooKeeper zookeeper;

    public static void main(String[] args) throws IOException, InterruptedException {
        Prop prop = PropContainer.getProp("host.properties");
        zookeeper = new ZooKeeper(prop.get("localhost"), prop.getInt("timeout"), new CreateNodeSync());
        System.out.println(zookeeper.getState());
        Thread.sleep(Integer.MAX_VALUE);
    }

    private void doSomething() {
        try {
            // Ids.OPEN_ACL_UNSAFE 任何人可以对这个节点进行任何操作
            String path = zookeeper.create("/node2", "123".getBytes(), ZooDefs.Ids.OPEN_ACL_UNSAFE, CreateMode.PERSISTENT);
            System.out.println("return path:" + path);
        } catch (KeeperException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        System.out.println("do something");
    }

    @Override
    public void process(WatchedEvent event) {
        System.out.println("收到事件：" + event);
        if (event.getState() == Watcher.Event.KeeperState.SyncConnected) {
            doSomething();
        }
    }
}
