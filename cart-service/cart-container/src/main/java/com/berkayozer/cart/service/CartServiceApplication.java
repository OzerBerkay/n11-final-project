package com.berkayozer.cart.service;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.data.redis.repository.configuration.EnableRedisRepositories;

@SpringBootApplication(scanBasePackages = "com.berkayozer")
@EnableRedisRepositories(basePackages = "com.berkayozer.cart.service.dataaccess.repository")
@EnableFeignClients(basePackages = "com.berkayozer.cart.service.infrastructure.feign")
public class CartServiceApplication {
    public static void main(String[] args) {
        SpringApplication.run(CartServiceApplication.class, args);
    }
}