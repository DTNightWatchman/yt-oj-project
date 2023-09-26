package com.yt.ytojbackenduserservice;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.EnableAspectJAutoProxy;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.stereotype.Component;

@SpringBootApplication
@MapperScan("com.yt.ytojbackenduserservice.mapper")
@EnableScheduling
@EnableAspectJAutoProxy(proxyTargetClass = true, exposeProxy = true)
@ComponentScan("com.yt")
@EnableDiscoveryClient
@EnableFeignClients(basePackages = {"com.yt.ytojbackendserviceclient.service"})
public class YtojBackendUserServiceApplication {

    public static void main(String[] args) {
        SpringApplication.run(YtojBackendUserServiceApplication.class, args);
    }

}
