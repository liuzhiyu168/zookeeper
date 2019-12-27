package com.zookeeper.zookeeper.service;

import java.util.concurrent.CountDownLatch;

/**
 * @auth liuzhiyu
 * @date 2019/12/18 10:07
 */
public class OrderService implements Runnable{

    private OrderNumGenerator orderNumGenerator = new OrderNumGenerator(); //定义成全局的

    private static CountDownLatch countDownLatch = new CountDownLatch(10);

//    public void run() {
//        try{
//            countDownLatch.await();
//        }catch(Exception e){
//            e.printStackTrace();
//        }
//        getNumber();
//    }

    public void getNumber(){
        String number = orderNumGenerator.getNumber();
        System.out.println(Thread.currentThread().getName()+";num："+number);
    }

    public static void main(String[] args) {
        OrderService orderService = new OrderService();
        for (int i = 0; i < 10; i++) {
            new Thread(orderService).start();
            countDownLatch.countDown();
        }
    }

    @Override
    public void run() {
        ZkDistributedLock lock = new ZkDistributedLock();
        try {
            lock.lock();
            //运到zk连接超时一定要手动回滚事务
            getNumber();
        } finally {
            lock.unLock();
        }
    }

}
