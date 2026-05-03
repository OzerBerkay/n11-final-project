package com.berkayozer.product.service.domain;

import com.berkayozer.product.service.domain.entity.Product;
import com.berkayozer.product.service.domain.event.ProductInformationEvent;
import lombok.extern.slf4j.Slf4j;

import java.time.ZoneId;
import java.time.ZonedDateTime;

import static com.berkayozer.domain.DomainConstants.UTC;

@Slf4j
public class ProductDomainServiceImpl implements ProductDomainService{

    @Override
    public ProductInformationEvent initializeProduct(Product product) {
        product.initializeProduct();
        log.info("Product with id: {} is initiated", product.getId().getValue());

        return new ProductInformationEvent(product, ZonedDateTime.now(ZoneId.of(UTC)));
    }
}
