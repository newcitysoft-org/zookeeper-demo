package com.newcitysoft.study.zookeeper.api;

import com.newcitysoft.study.zookeeper.util.Prop;
import com.newcitysoft.study.zookeeper.util.PropContainer;
import org.apache.zookeeper.AsyncCallback;
import org.apache.zookeeper.CreateMode;
import org.apache.zookeeper.WatchedEvent;
import org.apache.zookeeper.Watcher;
import org.apache.zookeeper.ZooDefs;
import org.apache.zookeeper.ZooKeeper;


import java.io.IOException;

/**
 * 异步创建znode
 * @author lixin.tian@renren-inc.com
 * @date 2018/2/28 11:49
 */
public class CreateNodeAsync implements Watcher{
    private static ZooKeeper zk;

    public static void main(String[] args) throws IOException, InterruptedException {
        Prop prop = PropContainer.getProp("host.properties");

        zk = new ZooKeeper(prop.get("localhost"), prop.getInt("timeout"), new CreateNodeAsync());

        System.out.println(zk.getState());
        Thread.sleep(Integer.MAX_VALUE);
    }

    @Override
    public void process(WatchedEvent event) {
        System.out.println("收到事件：" + event);
        if (event.getState() == Event.KeeperState.SyncConnected) {
            doSomething();
        }
    }

    private AsyncCallback.StringCallback stringCallback = new AsyncCallback.StringCallback() {

        /**
         * @param rc
         *            返回码0表示成功
         * @param path
         *            我们需要创建的节点的完整路径
         * @param ctx
         *            上面传入的值("创建")
         * @param name
         *            服务器返回给我们已经创建的节点的真实路径,如果是顺序节点path和name是不一样的
         */
        @Override
        public void processResult(int rc, String path, Object ctx, String name) {
            StringBuilder sb = new StringBuilder();
            sb.append("rc=" + rc).append("\n");
            sb.append("path=" + path).append("\n");
            sb.append("ctx=" + ctx).append("\n");
            sb.append("name=" + name);
            System.out.println(sb.toString());
        }
    };

    private void doSomething() {
        zk.create("/node3", "123".getBytes(), ZooDefs.Ids.OPEN_ACL_UNSAFE, CreateMode.PERSISTENT, stringCallback, "创建");
    }
}
