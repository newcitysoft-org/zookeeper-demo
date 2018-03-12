package com.newcitysoft.study.zookeeper.technology.api;

import org.apache.zookeeper.AsyncCallback;
import org.apache.zookeeper.CreateMode;
import org.apache.zookeeper.KeeperException;
import org.apache.zookeeper.Op;
import org.apache.zookeeper.OpResult;
import org.apache.zookeeper.ZooKeeper;
import org.apache.zookeeper.data.ACL;

import java.util.ArrayList;
import java.util.List;

/**
 * @author lixin.tian@renren-inc.com
 * @date 2018/3/2 10:48
 */
public class Transaction {
    private ZooKeeper zk;
    private List<Op> ops = new ArrayList();

    public Transaction(ZooKeeper zk) {
        this.zk = zk;
    }

    public Transaction create(String path, byte[] data, List<ACL> acl, CreateMode createMode) {
        this.ops.add(Op.create(path, data, acl, createMode.toFlag()));
        return this;
    }

    public Transaction delete(String path, int version) {
        this.ops.add(Op.delete(path, version));
        return this;
    }

    public Transaction check(String path, int version) {
        this.ops.add(Op.check(path, version));
        return this;
    }

    public Transaction setData(String path, byte[] data, int version) {
        this.ops.add(Op.setData(path, data, version));
        return this;
    }

    public List<OpResult> commit() throws InterruptedException, KeeperException {
        return this.zk.multi(this.ops);
    }

    public void commit(AsyncCallback.MultiCallback cb, Object ctx) {
        this.zk.multi(this.ops, cb, ctx);
    }
}
