package com.yt.ytojbackendjudgeservice.rabbitmq;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;
import lombok.extern.slf4j.Slf4j;

import java.io.IOException;
import java.util.concurrent.TimeoutException;

/**
 * @Author: YT
 * @Description: 初始化消息队列
 * @DateTime: 2023/9/26$ - 21:30
 */
@Slf4j
public class InitRabbitMq {

    public static void main(String[] args) {
        doInit();
    }

    public static void doInit() {
        try {
            ConnectionFactory factory = new ConnectionFactory();
            factory.setHost("localhost");
            Connection connection = factory.newConnection();
            Channel channel = connection.createChannel();
            String EXCHANGE_NAME = "judge_service_exchange";
            channel.exchangeDeclare(EXCHANGE_NAME, "direct");

            // 创建队列，随机分配一个队列名
            String queueName = "judge_service_queue";
            channel.queueDeclare(queueName, true, false, false, null);
            channel.queueBind(queueName, EXCHANGE_NAME, "judge_service_routingKey");
            log.info("启动成功");
        } catch (IOException | TimeoutException e) {
            log.error("启动失败");
        }
    }
}
