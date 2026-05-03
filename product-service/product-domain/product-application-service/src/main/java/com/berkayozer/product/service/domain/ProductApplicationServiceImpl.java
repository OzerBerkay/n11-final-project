package com.berkayozer.product.service.domain;

import com.berkayozer.product.service.domain.dto.create.CreateProductCommand;
import com.berkayozer.product.service.domain.dto.create.CreateProductResponse;
import com.berkayozer.product.service.domain.dto.read.ProductDetailResponse;
import com.berkayozer.product.service.domain.dto.read.ProductListResponse;
import com.berkayozer.product.service.domain.dto.read.ProductSearchQuery;
import com.berkayozer.product.service.domain.dto.update.UpdateProductCommand;
import com.berkayozer.product.service.domain.handler.CreateProductCommandHandler;
import com.berkayozer.product.service.domain.handler.ProductQueryHandler;
import com.berkayozer.product.service.domain.handler.UpdateProductCommandHandler;
import com.berkayozer.product.service.domain.ports.input.service.ProductApplicationService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;

import java.util.UUID;

@Slf4j
@Validated
@Service
class ProductApplicationServiceImpl implements ProductApplicationService {

    private final CreateProductCommandHandler createProductCommandHandler;
    private final UpdateProductCommandHandler updateProductCommandHandler;
    private final ProductQueryHandler productQueryHandler;

    public ProductApplicationServiceImpl(CreateProductCommandHandler createProductCommandHandler, UpdateProductCommandHandler updateProductCommandHandler, ProductQueryHandler productQueryHandler) {
        this.createProductCommandHandler = createProductCommandHandler;
        this.updateProductCommandHandler = updateProductCommandHandler;
        this.productQueryHandler = productQueryHandler;
    }

    @Override
    public CreateProductResponse createProduct(CreateProductCommand createProductCommand) {
        return createProductCommandHandler.createProduct(createProductCommand);
    }

    @Override
    public void updateProduct(UUID productId, UpdateProductCommand command) {
        updateProductCommandHandler.updateProduct(productId, command);
    }

    @Override
    public ProductDetailResponse getProductById(UUID productId) {
        return productQueryHandler.getProductById(productId);
    }

    @Override
    public ProductListResponse getAllProducts(ProductSearchQuery query) {
        return productQueryHandler.getAllProducts(query);
    }
}