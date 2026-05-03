package com.berkayozer.product.service.domain.handler;

import com.berkayozer.domain.valueobject.ProductId;
import com.berkayozer.product.service.domain.dto.read.ProductDetailResponse;
import com.berkayozer.product.service.domain.dto.read.ProductListResponse;
import com.berkayozer.product.service.domain.dto.read.ProductSearchQuery;
import com.berkayozer.product.service.domain.entity.Product;
import com.berkayozer.product.service.domain.exception.ProductNotFoundException;
import com.berkayozer.product.service.domain.mapper.ProductDataMapper;
import com.berkayozer.product.service.domain.ports.output.repository.ProductRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@Slf4j
@Component
public class ProductQueryHandler {

    private final ProductRepository productRepository;
    private final ProductDataMapper productDataMapper;

    public ProductQueryHandler(ProductRepository productRepository, ProductDataMapper productDataMapper) {
        this.productRepository = productRepository;
        this.productDataMapper = productDataMapper;
    }

    @Transactional(readOnly = true)
    public ProductDetailResponse getProductById(UUID productId) {
        Product product = productRepository.findById(new ProductId(productId))
                .orElseThrow(() -> new ProductNotFoundException("Product not found with id: " + productId));

        return productDataMapper.productToProductDetailResponse(product);
    }

    @Transactional(readOnly = true)
    public ProductListResponse getAllProducts(ProductSearchQuery query) {
        return productRepository.findAll(query);
    }
}