package com.newcitysoft.study.zookeeper.module.technology.api;

import com.newcitysoft.study.zookeeper.util.Prop;
import com.newcitysoft.study.zookeeper.util.PropContainer;
import org.apache.zookeeper.AsyncCallback.DataCallback;
import org.apache.zookeeper.AsyncCallback.StatCallback;
import org.apache.zookeeper.AsyncCallback.StringCallback;
import org.apache.zookeeper.CreateMode;
import org.apache.zookeeper.KeeperException;
import org.apache.zookeeper.WatchedEvent;
import org.apache.zookeeper.Watcher;
import org.apache.zookeeper.Watcher.Event.EventType;
import org.apache.zookeeper.Watcher.Event.KeeperState;
import org.apache.zookeeper.ZooDefs;
import org.apache.zookeeper.ZooKeeper;
import org.apache.zookeeper.data.Stat;

import java.io.IOException;

/**
 * @author lixin.tian@renren-inc.com
 * @date 2018/3/1 9:51
 */
public class ApiDemo {
    private static ZooKeeper zooKeeper;
    private Prop prop = PropContainer.getHostProp();

    private Watcher watcher = new Watcher() {
        @Override
        public void process(WatchedEvent event) {
            if(event.getState() == KeeperState.SyncConnected) {
                if (event.getType() == EventType.None && null == event.getPath()) {
                    createNode("/master");
                }else if(event.getType() == EventType.NodeDataChanged){
                    System.out.println(String.format("节点（%s）的数据被改变！", event.getPath()));
                }
            }
        }
    };

    private void start() throws IOException {
        zooKeeper = new ZooKeeper(prop.get("localhost"), prop.getInt("timeout"), watcher);
        try {
            Thread.sleep(Integer.MAX_VALUE);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    StringCallback stringCallback = new StringCallback() {
        @Override
        public void processResult(int rc, String path, Object ctx, String name) {
            System.out.println(ctx);
            System.out.println(rc);
            switch (KeeperException.Code.get(rc)){
                case CONNECTIONLOSS:
                    createNode(path);
                    break;
                case NODEEXISTS:
                    isExists(path);
                    break;
                case OK:
                    getData(path);
                default:
                    break;
            }
        }
    };

    private void createNode(String path){
        System.out.println("创建节点");
        zooKeeper.create(path,"123".getBytes(), ZooDefs.Ids.OPEN_ACL_UNSAFE, CreateMode.PERSISTENT, stringCallback, path);
    }

    StatCallback statCallback = new StatCallback() {
        @Override
        public void processResult(int rc, String path, Object data, Stat stat) {
            switch (KeeperException.Code.get(rc)){
                case OK:
                    getData(path);
                    break;
                case CONNECTIONLOSS:
                    createNode(path);
                    break;
                default:
                    break;
            }
        }
    };


    private void isExists(String path){
        System.out.println("检测节点");
        zooKeeper.exists(path, watcher, statCallback, path);
    }

    DataCallback dataCallback = new DataCallback() {
        @Override
        public void processResult(int rc, String path, Object o, byte[] bytes, Stat stat) {
            System.out.println(String.format("数据为：%s", new String(bytes)));
        }
    };

    private void getData(String path){
        System.out.println("获取数据");
        zooKeeper.getData(path, watcher, dataCallback, path);
    }

    public static void main(String[] args){
        try {
            new ApiDemo().start();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
