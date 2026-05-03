package com.berkayozer.cart.service.infrastructure.feign;

import com.berkayozer.cart.service.infrastructure.feign.dto.ProductResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.UUID;

@FeignClient(name = "product-service", url = "${product-service.url}")
public interface ProductFeignClient {

    // Artık sadece fiyatı değil, tüm ürünü çekiyoruz
    @GetMapping("/api/v1/products/{productId}")
    ProductResponse getProductById(@PathVariable("productId") UUID productId);
}