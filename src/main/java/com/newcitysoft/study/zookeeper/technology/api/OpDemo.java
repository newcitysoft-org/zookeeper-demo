package com.newcitysoft.study.zookeeper.technology.api;

import com.newcitysoft.study.zookeeper.util.Prop;
import com.newcitysoft.study.zookeeper.util.PropContainer;
import org.apache.zookeeper.AsyncCallback;
import org.apache.zookeeper.CreateMode;
import org.apache.zookeeper.KeeperException;
import org.apache.zookeeper.Op;
import org.apache.zookeeper.OpResult;
import org.apache.zookeeper.WatchedEvent;
import org.apache.zookeeper.Watcher;
import org.apache.zookeeper.ZooDefs;
import org.apache.zookeeper.ZooKeeper;
import org.apache.zookeeper.Watcher.Event.EventType;
import org.apache.zookeeper.data.Stat;


import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * @author lixin.tian@renren-inc.com
 * @date 2018/3/2 10:15
 */
public class OpDemo {

    private static ZooKeeper zooKeeper;
    private static Prop prop = PropContainer.getHostProp();

    private Watcher watcher = new Watcher() {
        @Override
        public void process(WatchedEvent watchedEvent) {
            String path = watchedEvent.getPath();
            EventType eventType = watchedEvent.getType();
            System.out.println(path);
            System.out.println(eventType.toString());
            switch (eventType){
                case NodeCreated:
                    System.out.println(String.format("节点%s被创建", path));
                    break;
                case NodeDeleted:
                    System.out.println(String.format("节点%s被删除", path));
                    break;
                case NodeDataChanged:
                    System.out.println(String.format("节点%s被改变", path));
                    break;
                default:
                    break;
            }
        }
    };

    private OpDemo start() {
        try {
            zooKeeper = new ZooKeeper(prop.get("localhost"), prop.getInt("timeout"), watcher);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return this;
    }

    AsyncCallback.MultiCallback multiCallback = new AsyncCallback.MultiCallback() {
        @Override
        public void processResult(int i, String s, Object o, List<OpResult> list) {
            switch (KeeperException.Code.get(i)){
                case CONNECTIONLOSS:
                    op();
                    break;
                case NODEEXISTS:
                case BADARGUMENTS:
                    ops.clear();
                    delete();
                    op(ops);
                    break;
                case OK:
                    System.out.println("任务执行成功！");
//                    ops.clear();
//                    setData();
//                    op(ops);
                    break;
                default:
                    break;
            }
        }
    };


    private static Random random = new Random();
    private static List<Op> ops = new ArrayList<>();
    private static String path = "/op";
    private volatile int version = 0;

    private static byte[] getRandData(){
        return Integer.toHexString(random.nextInt()).getBytes();
    }

    private OpDemo setData(){
        ops.add(Op.setData(path, getRandData(), version++));
        return this;
    }

    private OpDemo create(){
        ops.add(Op.create(path, getRandData(), ZooDefs.Ids.OPEN_ACL_UNSAFE, CreateMode.PERSISTENT));
        return this;
    }

    private Stat stat = new Stat();

    AsyncCallback.DataCallback dataCallback = new AsyncCallback.DataCallback() {
        @Override
        public void processResult(int i, String s, Object o, byte[] bytes, Stat stat) {

        }
    };

    private OpDemo getData(){
        try {
            zooKeeper.getData(path, watcher, stat);
        } catch (KeeperException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        System.out.println("version:"+stat.getVersion());
        return this;
    }

    private OpDemo op(List<Op> ops){
        zooKeeper.multi(ops, multiCallback, null);
        return this;
    }

    private OpDemo op() {
        zooKeeper.multi(ops, multiCallback, null);
        return this;
    }

    private OpDemo delete(){
        ops.add((Op.delete("/op", version++)));
        return this;
    }


//    private void transcation() {
//        Transaction transaction = new Transaction(zooKeeper);
//        String path = "/op";
//
//        transaction.create(path, "123".getBytes(), ZooDefs.Ids.OPEN_ACL_UNSAFE, CreateMode.PERSISTENT);
//        transaction.setData(path, "234".getBytes(), 0);
//        transaction.check(path, 0);
//        transaction.delete(path, 0);
//
//        transaction.commit(multiCallback, null);
//
//    }

    public static void main(String[] args) throws InterruptedException, KeeperException {
        OpDemo opDemo = new OpDemo();
        opDemo.start().create().setData().op().getData();
        Thread.sleep(Integer.MAX_VALUE);
    }
}
