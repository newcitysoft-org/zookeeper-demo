package com.newcitysoft.study.zookeeper.module.zookeeper.technology.api;

import com.newcitysoft.study.zookeeper.util.Prop;
import com.newcitysoft.study.zookeeper.util.PropContainer;
import org.apache.zookeeper.KeeperException;
import org.apache.zookeeper.WatchedEvent;
import org.apache.zookeeper.Watcher;
import org.apache.zookeeper.ZooKeeper;
import org.apache.zookeeper.Watcher.Event.EventType;
import org.apache.zookeeper.Watcher.Event.KeeperState;
import org.apache.zookeeper.data.Stat;

import java.io.IOException;

/**
 * @author lixin.tian@renren-inc.com
 * @date 2018/2/28 12:57
 */
public class GetDataSync implements Watcher{
    private static ZooKeeper zooKeeper;
    private static Stat stat = new Stat();

    public static void main(String[] args) throws IOException, InterruptedException {
        Prop prop = PropContainer.getHostProp();
        zooKeeper = new ZooKeeper(prop.get("localhost"), prop.getInt("timeout"), new GetDataSync());
        Thread.sleep(Integer.MAX_VALUE);
    }

    private void doSomething(ZooKeeper zooKeeper) {
        try {
            System.out.println(new String(zooKeeper.getData("/node2", true, stat)));
        } catch (KeeperException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void process(WatchedEvent event) {
        System.out.println(event.getType());
        if(event.getState() == KeeperState.SyncConnected){
            if(event.getType() == EventType.None && null == event.getPath()){
                doSomething(zooKeeper);
            }else if(event.getType() == EventType.NodeDataChanged){
                try {
                    System.out.println(new String(zooKeeper.getData(event.getPath(), true, stat)));
                    System.out.println("stat:" + stat);
                } catch (KeeperException e) {
                    e.printStackTrace();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }
    }
}
