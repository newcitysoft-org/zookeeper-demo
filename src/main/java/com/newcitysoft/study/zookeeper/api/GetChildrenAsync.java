package com.newcitysoft.study.zookeeper.api;

import com.newcitysoft.study.zookeeper.util.Prop;
import com.newcitysoft.study.zookeeper.util.PropContainer;
import org.apache.zookeeper.AsyncCallback;
import org.apache.zookeeper.WatchedEvent;
import org.apache.zookeeper.Watcher;
import org.apache.zookeeper.ZooKeeper;
import org.apache.zookeeper.data.Stat;
import org.apache.zookeeper.Watcher.Event.EventType;
import org.apache.zookeeper.Watcher.Event.KeeperState;

import java.io.IOException;
import java.util.List;

/**
 * @author lixin.tian@renren-inc.com
 * @date 2018/2/28 12:31
 */
public class GetChildrenAsync implements Watcher{
    private static ZooKeeper zooKeeper;

    public static void main(String[] args) throws IOException, InterruptedException {
        Prop prop = PropContainer.getHostProp();
        zooKeeper = new ZooKeeper(prop.get("localhost"), prop.getInt("timeout"), new GetChildrenAsync());
        System.out.println(zooKeeper.getState());
        Thread.sleep(Integer.MAX_VALUE);
    }

    private static AsyncCallback.Children2Callback children2Callback = new AsyncCallback.Children2Callback() {
        @Override
        public void processResult(int rc, String path, Object ctx, List<String> children, Stat stat) {
            StringBuilder sb = new StringBuilder();
            sb.append("rc=" + rc).append("\n");
            sb.append("path=" + path).append("\n");
            sb.append("ctx=" + ctx).append("\n");
            sb.append("children=" + children).append("\n");
            sb.append("stat=" + stat).append("\n");
            System.out.println(sb.toString());
        }
    };

    @Override
    public void process(WatchedEvent event) {
        System.out.println(event.getType());
        if(event.getState() == KeeperState.SyncConnected){
            if(event.getType() == EventType.None && null == event.getPath()){
                doSomething(zooKeeper);
            }else if (event.getType() == EventType.NodeChildrenChanged) {
                getChildrenAndWatch(event.getPath());
            }
        }
    }

    private void getChildrenAndWatch(String path){
        zooKeeper.getChildren(path, true, children2Callback, null);
    }

    private void doSomething(ZooKeeper zookeeper) {
        getChildrenAndWatch("/node2");
    }
}
