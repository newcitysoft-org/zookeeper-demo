package com.newcitysoft.study.zookeeper.technology.curatorapi;

import com.newcitysoft.study.zookeeper.util.Prop;
import com.newcitysoft.study.zookeeper.util.PropContainer;
import org.apache.curator.RetryPolicy;
import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.CuratorFrameworkFactory;
import org.apache.curator.framework.api.BackgroundCallback;
import org.apache.curator.framework.api.CuratorEvent;
import org.apache.curator.retry.RetryUntilElapsed;
import org.apache.zookeeper.CreateMode;
import org.apache.zookeeper.data.Stat;

import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * @author lixin.tian@renren-inc.com
 * @date 2018/2/28 16:39
 */
public class ApiDemo {
    private static CuratorFramework client;
    private static Prop prop = PropContainer.getHostProp();
    private static Stat stat = new Stat();
    private static String NODE_PATH = "/node3";

    static {
        RetryPolicy retryPolicy = new RetryUntilElapsed(5000, 1000);
        client = CuratorFrameworkFactory
                .builder()
                .connectString(prop.get("localhost"))
                .sessionTimeoutMs(prop.getInt("timeout"))
                .connectionTimeoutMs(prop.getInt("timeout"))
                .retryPolicy(retryPolicy)
                .build();
    }

    public static void start(){
        client.start();
    }

    public static String createNode(String path, String data){
        try {
            return client
                    .create()
//                    .withProtection()
                    .withMode(CreateMode.EPHEMERAL)
                    .forPath(path,
                            data.getBytes());
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public static void deleteNode(String path){
        // guaranteed 保障机制，只要连接还在，就算删除失败了也回一直在后台尝试删除
        try {
            client.delete().guaranteed().deletingChildrenIfNeeded().withVersion(-1).forPath(path);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static List<String> getChildren(String path){
        try {
            return client.getChildren().forPath(path);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public static void getData(String path){

        try {
            byte[] ret = new byte[0];
            ret = client.getData().storingStatIn(stat).forPath(path);

            System.out.println(new String(ret));

            System.out.println(stat);
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    public static void updateData(String path, String data){
        try {
            client.setData().withVersion(stat.getVersion()).forPath(path, data.getBytes());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void checkExists(String path){
        try {
            Stat stat = client.checkExists().forPath(path);
            System.out.println(stat);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void checkExistsAsync(String path) throws Exception {
        // 异步调用每次都是创建一个线程，如果系统执行的异步调用比较多，会创建比较多的线程，这里我们需要使用线程池
        ExecutorService es = Executors.newFixedThreadPool(5);
        client.checkExists().inBackground(new BackgroundCallback() {

            public void processResult(CuratorFramework arg0, CuratorEvent arg1) throws Exception {
                Stat stat = arg1.getStat();
                System.out.println(stat);
                System.out.println("ResultCode = " + arg1.getResultCode());
                System.out.println("Context = " + arg1.getContext());
            }
        }, "Context Value", es).forPath("/node1");

    }

    public static void main(String[] args) throws InterruptedException {
        // 1.启动
        start();
        // 2.创建
        //String path = createNode(NODE_PATH, "123");
        // 3.删除
        //deleteNode(path);
        // 4.获取子节点
//        createNode(NODE_PATH+"/child1", "012");
//        createNode(NODE_PATH+"/child2", "123");
//        createNode(NODE_PATH+"/child3", "234");
//        createNode(NODE_PATH+"/child4", "345");
        List<String> list = getChildren(NODE_PATH);
        list.forEach(_path->{
            System.out.println(_path);
        });

        getData(NODE_PATH);
        updateData(NODE_PATH, "234");
        getData(NODE_PATH);
        updateData(NODE_PATH, "456");
        getData(NODE_PATH);

        Thread.sleep(Integer.MAX_VALUE);

        checkExists(NODE_PATH);
    }
}
