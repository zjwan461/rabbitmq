package com.itsu.rabbitmq.routing;

import com.itsu.rabbitmq.utils.RabbitConstant;
import com.itsu.rabbitmq.utils.RabbitMQUtils;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;

import java.io.IOException;
import java.util.Scanner;
import java.util.concurrent.TimeoutException;

/**
 * @author 苏犇
 * @date 2019/7/7 18:53
 */

public class WeatherBureau {

    public static void main(String[] args) throws IOException, TimeoutException {
        Connection connection = RabbitMQUtils.getConnection();
        String input = new Scanner(System.in).next();
        Channel channel = connection.createChannel();
//        在发布/订阅模式下，channel不直接和消息队列打交道，而是通过绑定交换机发布消息
//        在消费者那一边则需要自己订阅才可以收到消息
        channel.basicPublish(RabbitConstant.EXCHANGE_WEATHER, "", null, input.getBytes());
        channel.close();
        connection.close();
    }
}
