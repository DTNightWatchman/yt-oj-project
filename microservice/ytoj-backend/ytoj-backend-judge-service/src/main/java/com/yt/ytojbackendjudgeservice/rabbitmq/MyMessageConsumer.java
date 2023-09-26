package com.yt.ytojbackendjudgeservice.rabbitmq;

import com.rabbitmq.client.Channel;
import com.yt.ytojbackendjudgeservice.judge.JudgeService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.amqp.support.AmqpHeaders;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.io.IOException;


/**
 * @Author: YT
 * @Description: rabbitMQ的消费者
 * @DateTime: 2023/9/26$ - 21:44
 */
@Component
@Slf4j
public class MyMessageConsumer {

    @Resource
    private JudgeService judgeService;

    //MANUAL 是手动确认
    @RabbitListener(queues = {"judge_service_queue"}, ackMode = "MANUAL")
    public void receiveMessage(String message, Channel channel, @Header(AmqpHeaders.DELIVERY_TAG) long deliveryTag) {
        log.info("receiveMessage message = {}", message);
        if (StringUtils.isNotBlank(message)) {
            long questionSubmitId = Long.parseLong(message);
            try {
                judgeService.doJudge(questionSubmitId);
                channel.basicAck(deliveryTag, false);
            } catch (IOException e) {
                try {
                    channel.basicNack(deliveryTag, false, true);
                } catch (IOException ex) {
                    log.error("放入重新消费失败");
                }
                log.error("确认消息失败");
            }
        }
    }
}
