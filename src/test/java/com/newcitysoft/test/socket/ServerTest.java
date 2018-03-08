package com.newcitysoft.test.socket;

import com.newcitysoft.study.zookeeper.module.scene.communication.multi.MiniServer;
import com.newcitysoft.study.zookeeper.module.scene.communication.simple.Const;

import java.io.IOException;

public class ServerTest {
    public static void main(String[] args) {

        try {
            MiniServer miniServer = new MiniServer(1234);
            miniServer.init(Const.PATH_SERVER);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}