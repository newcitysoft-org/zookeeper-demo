package com.newcitysoft.study.zookeeper.config;

import org.apache.zookeeper.KeeperException;

import java.io.IOException;
import java.util.Random;
import java.util.concurrent.TimeUnit;

/**
 * @author lixin.tian@renren-inc.com
 * @date 2018/3/6 10:41
 */
public class ConfigUpdater {

    public static final String PATH = "/config";

    private ConfigServer configServer;
    private Random random = new Random();

    public ConfigUpdater(String host) throws IOException {
        configServer = new ConfigServer(host);
        configServer.connect();
    }

    public void run() throws KeeperException, InterruptedException {
        while (true){
            String value = random.nextInt(100)+"";

            configServer.write(PATH, value);
            System.out.printf("Set %s to %s\n", PATH, value);
            TimeUnit.SECONDS.sleep(random.nextInt(10));
        }
    }

    public static void main(String[] args) throws IOException, KeeperException, InterruptedException {
        ConfigUpdater configUpdater = new ConfigUpdater("127.0.0.1:2181");
        configUpdater.run();
    }

}
