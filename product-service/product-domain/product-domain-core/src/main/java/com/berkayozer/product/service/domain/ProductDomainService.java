package com.berkayozer.product.service.domain;

import com.berkayozer.product.service.domain.entity.Product;
import com.berkayozer.product.service.domain.event.ProductInformationEvent;

public interface ProductDomainService {

    ProductInformationEvent initializeProduct(Product product);
}
