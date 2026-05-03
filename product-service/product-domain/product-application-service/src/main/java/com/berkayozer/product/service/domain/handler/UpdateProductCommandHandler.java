package com.berkayozer.product.service.domain.handler;

import com.berkayozer.domain.valueobject.Money;
import com.berkayozer.domain.valueobject.ProductId;
import com.berkayozer.product.service.domain.dto.update.UpdateProductCommand;
import com.berkayozer.product.service.domain.entity.Product;
import com.berkayozer.product.service.domain.exception.ProductNotFoundException;
import com.berkayozer.product.service.domain.ports.output.repository.ProductRepository;
import com.berkayozer.product.service.domain.valueobject.CategoryId;
import com.berkayozer.product.service.domain.valueobject.Stock;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@Slf4j
@Component
public class UpdateProductCommandHandler {

    private final ProductRepository productRepository;

    public UpdateProductCommandHandler(ProductRepository productRepository) {
        this.productRepository = productRepository;
    }

    @Transactional
    public void updateProduct(UUID productId, UpdateProductCommand command) {
        Product product = productRepository.findById(new ProductId(productId))
                .orElseThrow(() -> new ProductNotFoundException("Product not found with id: " + productId));

        product.updateProductDetails(
                command.getName(),
                command.getDescription(),
                new Money(command.getPrice()),
                new Stock(command.getStockQuantity()),
                new CategoryId(command.getCategoryId()),
                command.getBrand(),
                command.getModel(),
                command.getColor(),
                command.getImageUrl(),
                command.getActive()
        );

        // Save to database (Optimistic Lock is automatically activated here)
        Product savedProduct = productRepository.save(product);
        log.info("Product is updated with id: {}", savedProduct.getId().getValue());
    }
}