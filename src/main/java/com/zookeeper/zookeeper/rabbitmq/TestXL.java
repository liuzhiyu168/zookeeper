package com.zookeeper.zookeeper.rabbitmq;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

/**
 * @auth liuzhiyu
 * @date 2019/12/18 16:38
 */

@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest
public class TestXL {

    @Autowired
    private IMessageQueueService messageQueueService;

    @Test
    public void sendDelayMsg() throws Exception {
        messageQueueService.send(MQConstant.HELLO_QUEUE_NAME, "测试发送消息");
    }


    @Test
    public void sendDelayMsg2() throws Exception {
        messageQueueService.send(MQConstant.HELLO_QUEUE_NAME,"测试延迟发送消息",6000);
    }


}
