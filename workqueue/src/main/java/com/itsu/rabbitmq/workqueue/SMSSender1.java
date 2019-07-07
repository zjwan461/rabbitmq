package com.itsu.rabbitmq.workqueue;

import com.itsu.rabbitmq.utils.RabbitConstant;
import com.itsu.rabbitmq.utils.RabbitMQUtils;
import com.rabbitmq.client.*;

import java.io.IOException;

/**
 * @author 苏犇
 * @date 2019/7/7 15:21
 */

public class SMSSender1 {
    public static void main(String[] args) throws IOException {
        Connection connection = RabbitMQUtils.getConnection();
        Channel channel = connection.createChannel();
        channel.queueDeclare(RabbitConstant.QUEUE_SMS, false, false, false, null);
        //如果不写basicQos（1），则自动mq会自动将所有请求平均发送给所有消费者
//       basicqos ,MQ不再对消费者一次发送多个请求，而是消费者处理完一个消息后（确认后），再从队列中获取一个新的
        channel.basicQos(1);
        channel.basicConsume(RabbitConstant.QUEUE_SMS,false,new DefaultConsumer(channel){
            @Override
            public void handleDelivery(String consumerTag, Envelope envelope, AMQP.BasicProperties properties, byte[] body) throws IOException {
                String bodyStr = new String(body);
                System.out.println("SMSSENDER1-短信发送成功：" + bodyStr);
                try {
                    Thread.sleep(100);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                channel.basicAck(envelope.getDeliveryTag(), false);
            }
        });
    }
}
