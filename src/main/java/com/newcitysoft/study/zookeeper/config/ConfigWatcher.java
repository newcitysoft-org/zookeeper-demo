package com.newcitysoft.study.zookeeper.config;

import org.apache.zookeeper.KeeperException;
import org.apache.zookeeper.WatchedEvent;
import org.apache.zookeeper.Watcher;

import java.io.IOException;

/**
 * @author lixin.tian@renren-inc.com
 * @date 2018/3/6 10:32
 */
public class ConfigWatcher implements Watcher{
    private ConfigServer configServer;

    public ConfigWatcher(String path) throws IOException {
        configServer = new ConfigServer(path);

        configServer.connect();
    }

    public void display() throws KeeperException, InterruptedException {
        String value = configServer.read(ConfigUpdater.PATH, this);
        System.out.printf("Read %s as %s\n", ConfigUpdater.PATH, value);
    }

    @Override
    public void process(WatchedEvent event) {
        if(event.getType() == Event.EventType.NodeDataChanged){
            try {
                display();
            } catch (KeeperException e) {
                System.err.printf("KeeperException:%s.Exiting.\n", e);
            } catch (InterruptedException e) {
                System.err.println("Interrupted. Exiting.");
                Thread.currentThread().interrupt();
            }
        }
    }

    public static void main(String[] args) throws IOException, KeeperException, InterruptedException {
        ConfigWatcher configWatcher = new ConfigWatcher("127.0.0.1:2181");
        configWatcher.display();

        Thread.sleep(Long.MAX_VALUE);
    }
}
