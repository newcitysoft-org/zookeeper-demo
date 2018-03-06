package com.newcitysoft.study.zookeeper.module.scene.group;

import org.apache.zookeeper.KeeperException;

import java.util.List;
import java.util.Scanner;

public class ListGroup extends ConnectionWatcher {
    public void list(String groupName) throws KeeperException,
            InterruptedException {
        String path = "/" + groupName;
        try {
            List<String> children = zk.getChildren(path, false);
            if (children.isEmpty()) {
                System.out.printf("No members in group %s\n", groupName);
                System.exit(1);
            }
            for (String child : children) {
                System.out.println(child);
            }
        } catch (KeeperException.NoNodeException e) {
            System.out.printf("Group %s does not exist\n", groupName);
            System.exit(1);
        }
    }

    private static Scanner scanner = new Scanner(System.in);

    public static void main(String[] args) throws Exception {
        ListGroup listGroup = new ListGroup();
        System.out.println("Enter host location please.");
        listGroup.connect(scanner.next());
        System.out.println("Enter group name please.");
        listGroup.list(scanner.next());
        listGroup.close();
    }
}
