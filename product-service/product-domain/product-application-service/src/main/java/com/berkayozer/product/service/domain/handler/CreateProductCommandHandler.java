package com.berkayozer.product.service.domain.handler;

import com.berkayozer.product.service.domain.ProductDomainService;
import com.berkayozer.product.service.domain.dto.create.CreateProductCommand;
import com.berkayozer.product.service.domain.dto.create.CreateProductResponse;
import com.berkayozer.product.service.domain.entity.Product;
import com.berkayozer.product.service.domain.event.ProductInformationEvent;
import com.berkayozer.product.service.domain.exception.ProductDomainException;
import com.berkayozer.product.service.domain.mapper.ProductDataMapper;
import com.berkayozer.product.service.domain.ports.output.repository.ProductRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Component
public class CreateProductCommandHandler {

    private final ProductRepository productRepository;
    private final ProductDataMapper productDataMapper;
    private final ProductDomainService productDomainService;



    public CreateProductCommandHandler(ProductRepository productRepository,
                                       ProductDataMapper productDataMapper, ProductDomainService productDomainService) {
        this.productRepository = productRepository;
        this.productDomainService = productDomainService;
        this.productDataMapper = productDataMapper;
    }

    @Transactional
    public CreateProductResponse createProduct(CreateProductCommand command) {
        Product product = productDataMapper.createProductCommandToProduct(command);
        ProductInformationEvent productInformationEvent = productDomainService.initializeProduct(product);

        //DB Save
        Product savedProduct = productRepository.save(productInformationEvent.getProduct());
        if (savedProduct == null) {
            log.error("Could not save product with name: {}", product.getName());
            throw new ProductDomainException("Could not save product!");
        }

        log.info("Product is created with id: {}", savedProduct.getId().getValue());
        return productDataMapper.productToCreateProductResponse(savedProduct);
    }
}