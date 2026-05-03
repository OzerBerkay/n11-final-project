package com.berkayozer.product.service.config;

import com.berkayozer.product.service.domain.ProductDomainService;
import com.berkayozer.product.service.domain.ProductDomainServiceImpl;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class BeanConfiguration {

    @Bean
    public ProductDomainService productDomainService() {
        return new ProductDomainServiceImpl();
    }
}