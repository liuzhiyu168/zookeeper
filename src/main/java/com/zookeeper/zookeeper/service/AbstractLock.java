package com.zookeeper.zookeeper.service;

import java.util.concurrent.locks.Lock;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.Condition;

public abstract class AbstractLock implements Lock {

    @Override
    public void lock() {
        if (tryLock()) {
            System.out.println("#####" + Thread.currentThread().getName() + "获取锁成功######");
        } else {
            //一个拿到其他等待
            waitLock();
        }
    }

    /**
     * 获取锁失败进行等待
     */
    public abstract void waitLock();

    /**
     * 获取锁
     */
    public abstract boolean tryLock();

}
