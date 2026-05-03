package com.berkayozer.product.service;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@EnableJpaRepositories(basePackages = { "com.berkayozer.product.service.dataaccess"})
@EntityScan(basePackages = { "com.berkayozer.product.service.dataaccess"})
@SpringBootApplication(scanBasePackages = "com.berkayozer")
public class ProductServiceApplication {
    public static void main(String[] args) {
        SpringApplication.run(ProductServiceApplication.class, args);
    }
}