package com.newcitysoft.study.zookeeper.module.technology.zkclientapi;

import com.newcitysoft.study.zookeeper.util.Prop;
import com.newcitysoft.study.zookeeper.util.PropContainer;
import com.newcitysoft.study.zookeeper.module.technology.zkclientapi.model.Gender;
import com.newcitysoft.study.zookeeper.module.technology.zkclientapi.model.Student;
import org.I0Itec.zkclient.IZkChildListener;
import org.I0Itec.zkclient.IZkDataListener;
import org.I0Itec.zkclient.ZkClient;
import org.I0Itec.zkclient.serialize.SerializableSerializer;
import org.apache.zookeeper.CreateMode;
import org.apache.zookeeper.data.Stat;

import java.util.List;
import java.util.Random;

/**
 * @author lixin.tian@renren-inc.com
 * @date 2018/2/28 14:51
 */
public class ApiDemo {
    private static ZkClient zkClient;
    private static Prop prop = PropContainer.getHostProp();
    private static Student student;
    private static Stat stat = new Stat();

    static {
        student = new Student("lixin.tian", "120402033", Gender.MAIL);
        start();
    }

    private static Random random = new Random();
    public static String serverId = "lixin.tian";

    /**
     * 1.开启与zookeeper会话
     */
    private static void start(){
        zkClient = new ZkClient(prop.get("localhost"), prop.getInt("timeout"), prop.getInt("timeout"), new SerializableSerializer());
    }

    /**
     * 2.创建一个名为随机32以内的16进制位的znode，并存储一个学生类对象
     */
    public static String createNode(String path){
        return zkClient.create(path, student, CreateMode.PERSISTENT);
    }

    /**
     * 3.修改某节点数据
     * @param path
     */
    public static void updateNodeData(String path, Object object){
        zkClient.writeData(path, student);
    }

    /**
     * 4.获取节点数据
     * @param path
     */
    public static Student getNodeData(String path){
        return zkClient.readData(path, stat);
    }

    /**
     * 5.获取子节点
     * @param path
     * @return
     */
    public static List<String> getChildren(String path){
        return zkClient.getChildren(path);
    }

    /**
     * 6.判断节点是否存在
     * @param path
     * @return
     */
    public static boolean isExists(String path){
        return zkClient.exists(path);
    }

    /**
     * 7.删除节点
     * @param path
     * @return
     */
    public static boolean deleteNode(String path){
        return zkClient.delete(path, -1);
    }

    /**
     * 8.订阅子节点改变监控
     * @param path
     * @throws RuntimeException
     */
    public static void subscribeChildChanges(String path) throws RuntimeException{
        try {
            zkClient.subscribeChildChanges(path, new IZkChildListener() {
                @Override
                public void handleChildChange(String parentPath, List<String> currentChildren) throws Exception {
                    System.out.println(String.format("当前节点（%s）的子节点：%s", parentPath, currentChildren));
                }
            });
            Thread.sleep(Integer.MAX_VALUE);
        } catch (InterruptedException e) {
            new RuntimeException(e);
        }
    }

    /**
     * 9.订阅子节点改变监控
     * @param path
     */
    public static void subscribeDataChanges(String path){
        try {
            zkClient.subscribeDataChanges(path, new IZkDataListener() {
                @Override
                public void handleDataChange(String dataPath, Object data) throws Exception {
                    System.out.println(String.format("当前节点（%s）的数据变为%s", dataPath, data.toString()));
                }

                @Override
                public void handleDataDeleted(String dataPath) throws Exception {
                    System.out.println(String.format("当前节点（%s）的数据已改变", dataPath));
                }
            });
            Thread.sleep(Integer.MAX_VALUE);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args){
        // 1.先创建节点
        String path = "/"+serverId;
        if(!isExists(path)) {
            createNode(path);
        }
        System.out.println("root znode is created!--"+path);
        // 2.更改节点数据
        Student xiaohuan = new Student("xiaohuan", "120402055", Gender.FEMAIL);
        updateNodeData(path, xiaohuan);
        // 3.读取数据
        Student student = getNodeData(path);
        System.out.println(student.toString());
        // 4.获取子节点
        createNode(path+"/child1");
        createNode(path+"/child2");
        createNode(path+"/child3");
        List<String> list = getChildren(path);
        System.out.println(list);
        // 5.判断节点
        String result = isExists(path)==true?"存在":"不存在";
        System.out.println(String.format("节点%s%s", path, result));
        // 6.删除path下的所有子节点
        list.forEach(node->{
            System.out.println(path+"/"+node);
            deleteNode(path+"/"+node);
        });
        List<String> list2 = getChildren(path);
        System.out.println(list2);
        //subscribeChildChanges(path);
        subscribeDataChanges(path);
    }

}
