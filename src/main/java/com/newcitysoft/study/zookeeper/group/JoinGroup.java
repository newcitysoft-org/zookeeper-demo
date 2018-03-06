package com.newcitysoft.study.zookeeper.group;

import org.apache.zookeeper.CreateMode;
import org.apache.zookeeper.KeeperException;
import org.apache.zookeeper.ZooDefs;

import java.util.Scanner;

public class JoinGroup extends ConnectionWatcher {
    public void join(String groupName, String memberName) throws KeeperException,
            InterruptedException {
        String path = "/" + groupName + "/" + memberName;
        String createdPath = zk.create(path, null, ZooDefs.Ids.OPEN_ACL_UNSAFE,
                CreateMode.EPHEMERAL);
        System.out.println("Created " + createdPath);
    }

    private static Scanner scanner = new Scanner(System.in);

    public static void main(String[] args) throws Exception {
        JoinGroup joinGroup = new JoinGroup();
        System.out.println("Enter host location please.");
        joinGroup.connect(scanner.next());
        System.out.println("Enter group name please.");
        String group = scanner.next();
        System.out.println("Enter member name please.");
        String member = scanner.next();
        joinGroup.join(group, member);
        // stay alive until process is killed or thread is interrupted
        Thread.sleep(Long.MAX_VALUE);
    }
}