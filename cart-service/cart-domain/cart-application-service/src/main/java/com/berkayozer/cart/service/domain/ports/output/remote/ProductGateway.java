package com.berkayozer.cart.service.domain.ports.output.remote;

import com.berkayozer.domain.valueobject.Money;
import com.berkayozer.domain.valueobject.ProductId;

import java.util.List;
import java.util.Map;

public interface ProductGateway {
    // Verilen ürün ID'lerinin güncel fiyatlarını Product servisinden çeker
    Map<ProductId, Money> getCurrentPrices(List<ProductId> productIds);

    // Tek bir ürünün fiyatını çeker
    Money getProductPrice(ProductId productId);
}