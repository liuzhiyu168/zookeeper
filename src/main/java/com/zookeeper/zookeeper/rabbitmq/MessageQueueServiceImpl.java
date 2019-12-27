package com.zookeeper.zookeeper.rabbitmq;

import org.springframework.amqp.AmqpException;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.core.MessagePostProcessor;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * 
 * @author victor
 * @desc 消息队列服务接口实现
 */
@Service("messageQueueService")
public class MessageQueueServiceImpl implements IMessageQueueService{
	
	@Autowired
	private RabbitTemplate rabbitTemplate;
 
	@Override
	public void send(String queueName, String msg) {
        System.out.println("发送消息:"+ msg + ";发送时间:"+ new SimpleDateFormat("YYYY-MM-dd HH:mm:ss").format(new Date()));
        rabbitTemplate.convertAndSend(MQConstant.DEFAULT_EXCHANGE,queueName, msg);
	}


	@Override
	public void send(String queueName, String msg, long times) {
		MessagePostProcessor processor = new MessagePostProcessor(){
			@Override
			public Message postProcessMessage(Message message) throws AmqpException {
				message.getMessageProperties().setExpiration(times + "");
				return message;
			}
		};
        System.out.println("发送消息:"+ msg + ";发送时间:"+ new SimpleDateFormat("YYYY-MM-dd HH:mm:ss").format(new Date()));
		rabbitTemplate.convertAndSend(MQConstant.DEFAULT_EXCHANGE,MQConstant.DEFAULT_DEAD_LETTER_QUEUE_NAME, msg , processor);
	}

}