package com.berkayozer.product.service.domain.ports.input.service;

import com.berkayozer.product.service.domain.dto.create.CreateProductCommand;
import com.berkayozer.product.service.domain.dto.create.CreateProductResponse;
import com.berkayozer.product.service.domain.dto.read.ProductDetailResponse;
import com.berkayozer.product.service.domain.dto.read.ProductListResponse;
import com.berkayozer.product.service.domain.dto.read.ProductSearchQuery;
import com.berkayozer.product.service.domain.dto.update.UpdateProductCommand;

import java.util.UUID;

public interface ProductApplicationService {
    // Will be expanded with updatePrice, updateStock etc.
    CreateProductResponse createProduct(CreateProductCommand createProductCommand);
    void updateProduct(UUID productId, UpdateProductCommand command);
    ProductDetailResponse getProductById(UUID productId);
    ProductListResponse getAllProducts(ProductSearchQuery query);
}