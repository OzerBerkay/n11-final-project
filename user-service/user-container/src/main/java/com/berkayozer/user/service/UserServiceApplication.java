package com.berkayozer.user.service;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

// We're telling Spring to "Scan all berkayozer packages, not just the user.service package"
// This way, the GlobalExceptionHandler in the common module will also be included in the Spring context
@SpringBootApplication(scanBasePackages = "com.berkayozer")
@EnableJpaRepositories(basePackages = "com.berkayozer.user.service.dataaccess.user.repository")
@EntityScan(basePackages = "com.berkayozer.user.service.dataaccess.user.entity")
public class UserServiceApplication {
    public static void main(String[] args) {
        SpringApplication.run(UserServiceApplication.class, args);
    }
}