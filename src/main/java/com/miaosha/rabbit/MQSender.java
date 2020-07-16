package com.miaosha.rabbit;

import com.miaosha.redis.RedisService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.core.AmqpTemplate;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.core.MessageProperties;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class MQSender {

	private static Logger log = LoggerFactory.getLogger(MQSender.class);
	
	@Autowired
	AmqpTemplate amqpTemplate ;
	
	public void miaoshaSend(Object message) {
		String msg = RedisService.beanToString(message);
		log.info("send message:"+message);
		amqpTemplate.convertAndSend(MQConfig.MIAOSHA_QUEUE, msg);
	}
	
	//1
	public void send(Object message) {
//		String msg = RedisService.beanToString(message);
		log.info("send message:"+message);
		amqpTemplate.convertAndSend(MQConfig.QUEUE, message);
	}
	
	//2
	public void sendTopic(Object message) {
//		String msg = RedisService.beanToString(message);
		log.info("send topic message:"+message);
		amqpTemplate.convertAndSend(MQConfig.TOPIC_EXCHANGE, "topic.key1", message+"1");
		amqpTemplate.convertAndSend(MQConfig.TOPIC_EXCHANGE, "topic.key2", message+"2");
	}
	
	//3
	public void sendFanout(Object message) {
//		String msg = RedisService.beanToString(message);
		log.info("send fanout message:"+message);
		amqpTemplate.convertAndSend(MQConfig.FANOUT_EXCHANGE, "", message);
	}
	//4
	public void sendHeader(Object message) {
		String msg = RedisService.beanToString(message);
		log.info("send fanout message:"+msg);
		MessageProperties properties = new MessageProperties();
		properties.setHeader("header1", "value1");
		properties.setHeader("header2", "value2");
		Message obj = new Message(msg.getBytes(), properties);
		amqpTemplate.convertAndSend(MQConfig.HEADERS_EXCHANGE, "", obj);
	}

	
	
}
