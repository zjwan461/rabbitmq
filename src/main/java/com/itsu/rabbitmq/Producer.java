package com.itsu.rabbitmq;

import com.itsu.rabbitmq.utils.RabbitConstant;
import com.itsu.rabbitmq.utils.RabbitMQUtils;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;

import java.io.IOException;
import java.util.concurrent.TimeoutException;

/**
 * Created by 苏犇 on 2019/2/5. <br>
 */
public class Producer {
    public static void main(String[] args) throws IOException, TimeoutException {
//        //ConnectionFactory用于创建MQ的物理连接
//        ConnectionFactory connectionFactory = new ConnectionFactory();
//        connectionFactory.setHost("127.0.0.1");
//        connectionFactory.setPort(5672);//5672是rabbitmq的默认端口号
//        connectionFactory.setUsername("guest");
//        connectionFactory.setPassword("guest");
//        connectionFactory.setVirtualHost("/test");
//        //TCP 物理连接
//        Connection conn = connectionFactory.newConnection();
        Connection conn = RabbitMQUtils.getConnection();
        //创建通信"通道"，相当于TCP中的虚拟连接
        Channel channel = conn.createChannel();
        //创建队列，声明并创建一个队列，如果队列已存在，测使用这个队列
        //第一个参数：队列名称ID
        //第二个参数：是否持久化，false对应不持久化数据，MQ停掉数据就会丢失
        //第三个参数：是否队列私有化，false代表所有消费者都可以访问，true代码只有第一次拥有它的消费者才能一直使用，其他消费者不可以使用
        //第四个：是否自动删除，false代表连接停掉后不自动删除这个队列
        //其他额外的参数，null
        channel.queueDeclare(RabbitConstant.QUEUE_HELLOWORLD, false, false, false, null);
        //exechange 交换机，暂时用不到，在后面进行发布订阅时才会用到
        //队列名称
        //额外的设置属性
        //最后一个参数是要传递的消息字节数组
        String message = "hello world";
        channel.basicPublish("", RabbitConstant.QUEUE_HELLOWORLD, null, message.getBytes());
        channel.close();
        conn.close();
        System.out.println("数据发送成功");
    }
}
