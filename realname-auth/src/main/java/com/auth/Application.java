package com.auth;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.netflix.hystrix.EnableHystrix;
import org.springframework.cloud.netflix.hystrix.dashboard.EnableHystrixDashboard;
//import org.springframework.cloud.netflix.zuul.EnableZuulProxy;
import org.springframework.cloud.openfeign.EnableFeignClients;

@SpringBootApplication
@EnableDiscoveryClient
//@EnableZuulProxy
@EnableFeignClients
@EnableHystrix //@EnableCircuitBreaker开启监控端点，访问/actuator/hystrix.stream
@EnableHystrixDashboard //监控端点可视化，访问/hystrix
public class Application {

	public static void main(String[] args) {
        SpringApplication.run(Application.class, args);
    }
}
