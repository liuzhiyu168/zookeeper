package com.zookeeper.zookeeper.service;

import java.text.SimpleDateFormat;
import java.util.Date;

//生成订单号 时间戳
public class OrderNumGenerator {

     //区分不同的订单号
    private static int count = 0;

    //单台服务器，多个线程 同时生成订单号
    public String getNumber(){
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        return "=========订单号："+sdf.format(new Date()) + "-" + ++count;  //时间戳后面加了 count
    }
    
}