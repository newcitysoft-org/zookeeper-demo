package com.newcitysoft.study.zookeeper.module.scene.config;

import org.apache.zookeeper.CreateMode;
import org.apache.zookeeper.KeeperException;
import org.apache.zookeeper.WatchedEvent;
import org.apache.zookeeper.Watcher;
import org.apache.zookeeper.ZooDefs;
import org.apache.zookeeper.ZooKeeper;
import org.apache.zookeeper.data.Stat;

import java.io.IOException;
import java.nio.charset.Charset;

/**
 * @author lixin.tian@renren-inc.com
 * @date 2018/3/6 10:26
 */
public class ConfigServer {
    private ZooKeeper zooKeeper;
    private static int timeout = 5000;
    private String host;
    public static final Charset CHARSET = Charset.forName("UTF-8");

    public ConfigServer(String host){
        this.host = host;
    }

    private Watcher watcher = new Watcher() {
        @Override
        public void process(WatchedEvent event) {
            if(event.getState() == Event.KeeperState.SyncConnected){
                if(event.getType() == Event.EventType.NodeCreated){
                    System.out.println("节点别创建！");
                }
            }
        }
    };

    public void connect() throws IOException {
        zooKeeper = new ZooKeeper(host, timeout, watcher);
    }

    public void write(String path, String value) throws KeeperException, InterruptedException {
        Stat stat = zooKeeper.exists(path, watcher);
        if(stat == null){
            zooKeeper.create(path, value.getBytes(CHARSET), ZooDefs.Ids.OPEN_ACL_UNSAFE, CreateMode.PERSISTENT);
        }else{
            zooKeeper.setData(path, value.getBytes(CHARSET), -1);
        }
    }

    public String read(String path, Watcher watcher) throws KeeperException, InterruptedException {
        byte[] data = zooKeeper.getData(path, watcher, null);
        return new String(data, CHARSET);
    }

}
