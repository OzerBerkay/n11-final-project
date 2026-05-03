package com.berkayozer.product.service.dataaccess.product.mapper;

import com.berkayozer.domain.valueobject.Money;
import com.berkayozer.domain.valueobject.ProductId;
import com.berkayozer.product.service.dataaccess.product.entity.ProductEntity;
import com.berkayozer.product.service.domain.entity.Product;
import com.berkayozer.product.service.domain.valueobject.CategoryId;
import com.berkayozer.product.service.domain.valueobject.Stock;
import org.springframework.stereotype.Component;

@Component
public class ProductDataAccessMapper {

    public ProductEntity productToProductEntity(Product product) {
        return ProductEntity.builder()
                .id(product.getId().getValue())
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
                .version(product.getVersion())
                .build();
    }

    public Product productEntityToProduct(ProductEntity productEntity) {
        Product product = Product.Builder.builder()
                .name(productEntity.getName())
                .description(productEntity.getDescription())
                .price(new Money(productEntity.getPrice()))
                .stock(new Stock(productEntity.getStockQuantity()))
                .categoryId(new CategoryId(productEntity.getCategoryId()))
                .brand(productEntity.getBrand())
                .model(productEntity.getModel())
                .color(productEntity.getColor())
                .imageUrl(productEntity.getImageUrl())
                .version(productEntity.getVersion())
                .build();

        product.setId(new ProductId(productEntity.getId()));
        product.updateProductDetails(
                productEntity.getName(),
                productEntity.getDescription(),
                new Money(productEntity.getPrice()),
                new Stock(productEntity.getStockQuantity()),
                new CategoryId(productEntity.getCategoryId()),
                productEntity.getBrand(),
                productEntity.getModel(),
                productEntity.getColor(),
                productEntity.getImageUrl(),
                productEntity.getActive()
        );
        return product;
    }
}