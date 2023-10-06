package com.yt.ytojbackendjudgeservice;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.EnableAspectJAutoProxy;
import org.springframework.scheduling.annotation.EnableScheduling;

import static com.yt.ytojbackendjudgeservice.rabbitmq.InitRabbitMq.doInit;

@SpringBootApplication
@EnableScheduling
@EnableAspectJAutoProxy(proxyTargetClass = true, exposeProxy = true)
@ComponentScan("com.yt")
@EnableDiscoveryClient
@EnableFeignClients(basePackages = {"com.yt.ytojbackendserviceclient.service"})
public class YtojBackendJudgeServiceApplication {

    public static void main(String[] args) {
        //doInit();
        SpringApplication.run(YtojBackendJudgeServiceApplication.class, args);
    }

}
