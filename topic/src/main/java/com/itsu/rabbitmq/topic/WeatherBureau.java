package com.itsu.rabbitmq.topic;

import com.itsu.rabbitmq.utils.RabbitConstant;
import com.itsu.rabbitmq.utils.RabbitMQUtils;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;

import java.io.IOException;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.concurrent.TimeoutException;

/**
 * @author 苏犇
 * @date 2019/7/7 18:53
 */

public class WeatherBureau {

    public static void main(String[] args) throws IOException, TimeoutException {
//        有序的hash映射值
        Map area = new LinkedHashMap<String, String>();
        area.put("china.hebei.shijiazhuang.20991011", "中国河北石家庄20991011天气数据");
        area.put("china.guangdong.shenzhen.20991011", "中国广东深圳20991011天气数据");
        area.put("china.guangdong.guangzhou.20991011", "中国广东广州20991011天气数据");
        area.put("us.cal.la.20991011", "美国加州洛杉矶20991011天气数据");

        area.put("china.hebei.shijiazhuang.20991012", "中国河北石家庄20991012天气数据");
        area.put("china.guangdong.shenzhen.20991012", "中国广东深圳20991012天气数据");
        area.put("china.guangdong.guangzhou.20991012", "中国广东广州20991012天气数据");
        area.put("us.cal.la.20991012", "美国加州洛杉矶20991012天气数据");


        Connection connection = RabbitMQUtils.getConnection();
        Channel channel = connection.createChannel();
//        在发布/订阅模式下，channel不直接和消息队列打交道，而是通过绑定交换机发布消息
//        在消费者那一边则需要自己订阅才可以收到消息
        Iterator<Map.Entry<String, String>> iterator = area.entrySet().iterator();
        while (iterator.hasNext()) {
            Map.Entry<String, String> me = iterator.next();
            channel.basicPublish(RabbitConstant.EXCHANGE_WEATHER_TOPIC, me.getKey(), null, me.getValue().getBytes());
        }
        channel.close();
        connection.close();
    }
}
