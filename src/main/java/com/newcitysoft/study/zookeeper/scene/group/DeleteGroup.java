package com.newcitysoft.study.zookeeper.scene.group;

import org.apache.zookeeper.KeeperException;

import java.util.List;
import java.util.Scanner;

public class DeleteGroup extends ConnectionWatcher {
    public void delete(String groupName) throws KeeperException,
            InterruptedException {
        String path = "/" + groupName;
        try {
            List<String> children = zk.getChildren(path, false);
            for (String child : children) {
                zk.delete(path + "/" + child, -1);
            }
            zk.delete(path, -1);
        } catch (KeeperException.NoNodeException e) {
            System.out.printf("Group %s does not exist\n", groupName);
            System.exit(1);
        }
    }

    private static Scanner scanner = new Scanner(System.in);

    public static void main(String[] args) throws Exception {
        DeleteGroup deleteGroup = new DeleteGroup();
        System.out.println("Enter host location please.");
        deleteGroup.connect(scanner.next());
        System.out.println("Enter group name please.");
        deleteGroup.delete(scanner.next());
        deleteGroup.close();
    }
}
