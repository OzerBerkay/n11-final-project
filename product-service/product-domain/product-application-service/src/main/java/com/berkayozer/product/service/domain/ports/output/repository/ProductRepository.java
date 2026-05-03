package com.berkayozer.product.service.domain.ports.output.repository;

import com.berkayozer.domain.valueobject.ProductId;
import com.berkayozer.product.service.domain.dto.read.ProductListResponse;
import com.berkayozer.product.service.domain.dto.read.ProductSearchQuery;
import com.berkayozer.product.service.domain.entity.Product;

import java.util.Optional;

public interface ProductRepository {
    //TODO: checked
    Product save(Product product);
    Optional<Product> findById(ProductId productId);
    ProductListResponse findAll(ProductSearchQuery query);}