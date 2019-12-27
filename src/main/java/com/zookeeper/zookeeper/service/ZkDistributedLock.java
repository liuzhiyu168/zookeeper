package com.zookeeper.zookeeper.service;

import org.I0Itec.zkclient.IZkDataListener;
import org.I0Itec.zkclient.ZkClient;
import org.springframework.util.StringUtils;

import java.util.Collections;
import java.util.List;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.Condition;

public class ZkDistributedLock extends AbstractLock {

    private static final String CONNECTION = "127.0.0.1:2181";
    private static final int TIMEOUT = 5000;
    private ZkClient zkClient = new ZkClient(CONNECTION, TIMEOUT);
    private static final String LOCK_PATH = "/mike";
    private static final String REAL_PATH = LOCK_PATH + "/";
    private String currentPath;
    private String beforePath;
    private CountDownLatch countDownLatch = null;

    public void unLock() {
        if (zkClient != null) {
            //关闭连接会删除所有临时节点
            zkClient.close();
            System.out.println("#####" + Thread.currentThread().getName() + "释放锁成功######");
        }
    }

    @Override
    public boolean tryLock() {
        if (!zkClient.exists(LOCK_PATH)) {
            zkClient.createPersistent(LOCK_PATH);
        }
        if (StringUtils.isEmpty(currentPath)) {
            //创建分布式锁【临时顺序节点】
            currentPath = zkClient.createEphemeralSequential(REAL_PATH, "data");
            //获取所有子节点
            List<String> children = zkClient.getChildren(LOCK_PATH);
            Collections.sort(children);
            if (currentPath.equals(REAL_PATH + children.get(0))) {
                //如果是第一个节点则获取锁，否则进行监听上一个节点
                return true;
            } else {
                int i = Collections.binarySearch(children, currentPath.substring(REAL_PATH.length()));
                //获取当前节点的上一个节点
                beforePath = REAL_PATH + children.get(i - 1);
            }
        }
        return false;
    }

    @Override
    public void waitLock() {
        IZkDataListener listener = new IZkDataListener() {
            // zk事件监听节点修改
            @Override
            public void handleDataChange(String s, Object o) throws Exception {
            }

            //zk事件监听节点删除
            @Override
            public void handleDataDeleted(String s) throws Exception {
                System.out.println("--------------节点删除" + s);
                if (countDownLatch != null) {
                    // 计数器为0的情况，await 后面的继续执行,释放阻塞线程
                    countDownLatch.countDown();
                }
            }
        };
        // 监听上一个节点是否被删除，是就释放阻塞线程【轮到自己了】-》handleDataDeleted
        // 只有上一个节点被删除当前节点才会继续执行
        zkClient.subscribeDataChanges(beforePath, listener);
        if (zkClient.exists(beforePath)) {
            countDownLatch = new CountDownLatch(1);
            try {
                //阻塞其他线程，下面的代码不执行
                countDownLatch.wait();
            } catch (Exception e) {
            }
        }
        //此代码只要等beforePath节点删除后才会执行，既然删除了就没必要监听了
        zkClient.unsubscribeDataChanges(beforePath, listener);
    }

    @Override
    public boolean tryLock(long time, TimeUnit unit) {
        return false;
    }

    @Override
    public void unlock() {

    }

    @Override
    public Condition newCondition() {
        return null;
    }

    @Override
    public void lockInterruptibly() {

    }

}