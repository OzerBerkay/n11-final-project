package com.berkayozer.product.service.domain.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Data
@Configuration
@ConfigurationProperties(prefix = "product-service")
public class ProductServiceConfigData {
    private String productCreatedTopicName;
    private String productStockReservedTopicName;
    private String productStockFailedTopicName;
    private String orderCreatedTopicName;
    private String orderCancelledTopicName;
}