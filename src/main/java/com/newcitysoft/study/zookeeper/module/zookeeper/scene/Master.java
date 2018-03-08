package com.newcitysoft.study.zookeeper.module.zookeeper.scene;

import org.apache.zookeeper.AsyncCallback.StringCallback;
import org.apache.zookeeper.AsyncCallback.DataCallback;
import org.apache.zookeeper.CreateMode;
import org.apache.zookeeper.KeeperException;
import org.apache.zookeeper.KeeperException.*;
import org.apache.zookeeper.WatchedEvent;
import org.apache.zookeeper.Watcher;
import org.apache.zookeeper.ZooDefs;
import org.apache.zookeeper.ZooKeeper;
import org.apache.zookeeper.data.Stat;

import java.io.IOException;
import java.util.Random;

/**
 * @author lixin.tian@renren-inc.com
 * @date 2018/2/27 14:28
 */
public class Master implements Watcher{
    static Random random = new Random();
    String serverId = Integer.toHexString(random.nextInt());
    String hostPort;
    ZooKeeper zk;

    Master(String hostPort){
        this.hostPort = hostPort;
    }

    void startZK(){
        try {
            zk = new ZooKeeper(hostPort, 5000, this);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    void stopZK() throws Exception { zk.close(); }

    static boolean isLeader = false;

    /**
     * returns true if there is a master
     * @return
     */
//    boolean checkMaster() {
//        while (true) {
//            try {
//                Stat stat = new Stat();
//                byte[] data = zk.getData("/master", false, stat);
//                isLeader = new String(data).equals(serverId);
//                return true;
//            }  catch (NoNodeException e) {
//                // no master, so try create again
//                return false;
//            } catch (ConnectionLossException e) {
//            } catch (InterruptedException e) {
//                e.printStackTrace();
//            } catch (KeeperException e) {
//                e.printStackTrace();
//            }
//
//        }
//    }


    /**
     * 1.我们试着创建znode节点/master。如果这个znode节点存在，create
     * 就会失败。同时我们想在/master节点的数据字段保存对应这个服务器的唯一ID。
     * 2.数据字段只能存储字节数组类型的数据，所以我们将int型转换为一个字节数组。
     * 3.使用开放的ACL策略。
     * 4.我们创建的节点类型为EPHEMERAL。
     * @throws KeeperException
     * @throws InterruptedException
     */
    void runForMaster() {
        while (true){
            try {
                zk.create("/master",
                        serverId.getBytes(),
                        ZooDefs.Ids.OPEN_ACL_UNSAFE,
                        CreateMode.EPHEMERAL);
                isLeader = true;
                break;
            } catch (NodeExistsException e) {
                isLeader = false;
                break;
            } catch (ConnectionLossException e){

            }catch (InterruptedException e) {
                e.printStackTrace();
            } catch (KeeperException e) {
                e.printStackTrace();
            }
//            if (checkMaster()) {
//                break;
//            }
        }
    }

    @Override
    public void process(WatchedEvent watchedEvent) {
        System.out.println(watchedEvent);
    }

    StringCallback masterCreateCallback = new StringCallback() {
        @Override
        public void processResult(int rc, String path, Object ctx, String name) {
            switch(Code.get(rc)){
                case CONNECTIONLOSS:
                    checkMaster();
                    return;
                case OK:
                    isLeader = true;
                    break;
                default:
                    isLeader = false;
            }
            System.out.println("I'm " + (isLeader ? "" : "not ") +
                    "the leader");
        }
    };


    DataCallback masterCheckCallback = new DataCallback() {
        @Override
        public void processResult(int rc, String path, Object ctx, byte[] data,
                           Stat stat) {
            switch(Code.get(rc)) {
                case CONNECTIONLOSS:
                    checkMaster();
                    return;
                case NONODE:
                    runForMaster();
                    return;
            }
        }
    };


    void checkMaster() {
        zk.getData("/master", false, masterCheckCallback, null);
    }



    public static void main(String[] args) throws Exception {
        Master m = new Master("2223");
        m.startZK();
        m.runForMaster();
        if (isLeader) {
            System.out.println("I'm the leader");
            // wait for a bit
            Thread.sleep(60000);
        } else {
            System.out.println("Someone else is the leader");
        }
        m.stopZK();
    }

}
