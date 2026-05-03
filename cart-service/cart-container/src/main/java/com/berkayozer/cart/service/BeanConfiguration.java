package com.berkayozer.cart.service;

import com.berkayozer.cart.service.domain.CartDomainService;
import com.berkayozer.cart.service.domain.CartDomainServiceImpl;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class BeanConfiguration {

    @Bean
    public CartDomainService cartDomainService() {
        return new CartDomainServiceImpl();
    }
}