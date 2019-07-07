package com.itsu.rabbitmq;

import com.itsu.rabbitmq.utils.RabbitConstant;
import com.itsu.rabbitmq.utils.RabbitMQUtils;
import com.rabbitmq.client.*;

import java.io.IOException;
import java.util.concurrent.TimeoutException;

/**
 * Created by 苏犇 on 2019/2/5. <br>
 */
public class Consumer {
    public static void main(String[] args) throws IOException, TimeoutException {
//        ConnectionFactory connectionFactory = new ConnectionFactory();
//        connectionFactory.setHost("127.0.0.1");
//        connectionFactory.setPort(5672);
//        connectionFactory.setUsername("guest");
//        connectionFactory.setPassword("guest");
//        connectionFactory.setVirtualHost("/test");
//
//        Connection conn = connectionFactory.newConnection();
        Connection conn = RabbitMQUtils.getConnection();
        //创建通道
        Channel channel = conn.createChannel();
        //第一个参数：队列名称ID
        //第二个参数：是否持久化
//        第三个参数：是否私有化队列
//        第四个参数：是否自动删除，false代表连接停掉后不自动删除掉这个队列
        channel.queueDeclare(RabbitConstant.QUEUE_HELLOWORLD, false, false, false, null);
        //创建一个消息消费者
        //第二个参数代表是否自动确认收到消息，false代表手动编程来确认消息，这是MQ的推荐做法
        //第三个参数要传入DefaultConsumer的实现类
        channel.basicConsume(RabbitConstant.QUEUE_HELLOWORLD, false, new Reciver(channel));
    }
}

class Reciver extends DefaultConsumer {

    private Channel channel;

    //Channel通道对象需要从外层传入，在handlerDelivery中要用到
    public Reciver(Channel channel) {
        super(channel);
        this.channel = channel;
    }

    @Override
    public void handleDelivery(String consumerTag, Envelope envelope, AMQP.BasicProperties properties, byte[] body) throws IOException {
//        super.handleDelivery(consumerTag, envelope, properties, body);
        String messageBody = new String(body);
        System.out.println("消费者接收到 ：" + messageBody);
        //签收消息，确认消息
        //envelope.getDeliveryTag() 获取这个消息的tagid
        //false只确认签收当前的消息，设置为true的时候则代表签收该消费者所有未签收的消息
        channel.basicAck(envelope.getDeliveryTag(), false);
    }
}