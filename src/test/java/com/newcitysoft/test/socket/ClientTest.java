package com.newcitysoft.test.socket;

import com.newcitysoft.study.zookeeper.module.net.scene.communication.multi.MiniClient;

import java.io.File;
import java.io.IOException;

/**
 * @author lixin.tian@renren-inc.com
 * @date 2018/3/7 15:27
 */
public class ClientTest {
    public static void main(String[] args) {
        try {
            Thread.sleep(3000);
            MiniClient client = new MiniClient("localhost", 1234);
            client.init();
            client.setTitle("浙大科技园");
            client.addFile(new File("D:\\data\\socket\\client\\123.txt"));
//            client.addFile(new File("D:\\data\\socket\\client\\db.avi"));
//            client.addFile(new File("D:\\data\\socket\\client\\db.jpg"));
            String result = client.postAsync();
            System.out.println(result);
        } catch (IOException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
