package com.berkayozer.product.service.domain.mapper;

import com.berkayozer.domain.valueobject.Money;
import com.berkayozer.product.service.domain.dto.create.CreateProductCommand;
import com.berkayozer.product.service.domain.dto.create.CreateProductResponse;
import com.berkayozer.product.service.domain.dto.read.ProductDetailResponse;
import com.berkayozer.product.service.domain.entity.Product;
import com.berkayozer.product.service.domain.valueobject.CategoryId;
import com.berkayozer.product.service.domain.valueobject.Stock;
import org.springframework.stereotype.Component;


@Component
public class ProductDataMapper {

    public Product createProductCommandToProduct(CreateProductCommand command) {
        return Product.Builder.builder()
                .name(command.getName())
                .description(command.getDescription())
                .brand(command.getBrand())
                .model(command.getModel())
                .color(command.getColor())
                .price(new Money(command.getPrice()))
                .stock(new Stock(command.getInitialStock()))
                .categoryId(new CategoryId(command.getCategoryId()))
                .imageUrl(command.getImageUrl())
                .build();
    }

    public ProductDetailResponse productToProductDetailResponse(Product product) {
        return ProductDetailResponse.builder()
                .productId(product.getId().getValue())
                .name(product.getName())
                .description(product.getDescription())
                .price(product.getPrice().getAmount())
                .stockQuantity(product.getStock().getQuantity())
                .categoryId(product.getCategoryId().getValue())
                .brand(product.getBrand())
                .model(product.getModel())
                .color(product.getColor())
                .imageUrl(product.imageUrl())
                .active(product.isActive())
                .build();
    }

    public CreateProductResponse productToCreateProductResponse(Product product) {
        return CreateProductResponse.builder()
                .productId(product.getId().getValue())
                .build();
    }
}