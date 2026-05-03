package com.berkayozer.cart.service.infrastructure.adapter;

import com.berkayozer.cart.service.domain.ports.output.remote.ProductGateway;
import com.berkayozer.cart.service.infrastructure.feign.ProductFeignClient;
import com.berkayozer.cart.service.infrastructure.feign.dto.ProductResponse;
import com.berkayozer.domain.valueobject.Money;
import com.berkayozer.domain.valueobject.ProductId;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Slf4j
@Component
@RequiredArgsConstructor
public class ProductGatewayImpl implements ProductGateway {

    private final ProductFeignClient productFeignClient;

    @Override
    public Map<ProductId, Money> getCurrentPrices(List<ProductId> productIds) {
        Map<ProductId, Money> prices = new HashMap<>();

        for (ProductId productId : productIds) {
            try {
                // Sadece fiyatı değil, tüm ürünü çektik
                ProductResponse response = productFeignClient.getProductById(productId.getValue());
                // İçinden fiyatı aldık
                prices.put(productId, new Money(response.getPrice()));
            } catch (Exception e) {
                log.error("Could not fetch details for product: {}. Error: {}", productId.getValue(), e.getMessage());
            }
        }
        return prices;
    }

    @Override
    public Money getProductPrice(ProductId productId) {
        // Sadece fiyatı değil, tüm ürünü çektik
        ProductResponse response = productFeignClient.getProductById(productId.getValue());
        // İçinden fiyatı alıp döndük
        return new Money(response.getPrice());
    }
}