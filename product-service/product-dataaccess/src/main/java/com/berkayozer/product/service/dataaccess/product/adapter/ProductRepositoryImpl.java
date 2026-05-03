package com.berkayozer.product.service.dataaccess.product.adapter;

import com.berkayozer.domain.valueobject.ProductId;
import com.berkayozer.product.service.dataaccess.product.entity.ProductEntity;
import com.berkayozer.product.service.dataaccess.product.mapper.ProductDataAccessMapper;
import com.berkayozer.product.service.dataaccess.product.repository.ProductJpaRepository;
import com.berkayozer.product.service.domain.dto.read.ProductListResponse;
import com.berkayozer.product.service.domain.dto.read.ProductSearchQuery;
import com.berkayozer.product.service.domain.entity.Product;
import com.berkayozer.product.service.domain.ports.output.repository.ProductRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Component
public class ProductRepositoryImpl implements ProductRepository {

    private final ProductJpaRepository productJpaRepository;
    private final ProductDataAccessMapper productDataAccessMapper;

    public ProductRepositoryImpl(ProductJpaRepository productJpaRepository, ProductDataAccessMapper productDataAccessMapper) {
        this.productJpaRepository = productJpaRepository;
        this.productDataAccessMapper = productDataAccessMapper;
    }

    @Override
    public Product save(Product product) {
        return productDataAccessMapper.productEntityToProduct(
                productJpaRepository.save(productDataAccessMapper.productToProductEntity(product))
        );
    }

    @Override
    public Optional<Product> findById(ProductId productId) {
        return productJpaRepository.findById(productId.getValue())
                .map(productDataAccessMapper::productEntityToProduct);
    }

    @Override
    public ProductListResponse findAll(ProductSearchQuery query) {
        Pageable pageable = PageRequest.of(query.getPage(), query.getSize());
        Specification<ProductEntity> spec = ProductSpecification.getSpec(query);

        // Artık Spring Data JPA, sadece dolu olan Query alanlarına göre WHERE üretecek
        Page<ProductEntity> entityPage = productJpaRepository.findAll(spec, pageable);

        List<ProductListResponse.ProductSummary> productSummaries = entityPage.getContent().stream()
                .map(entity -> ProductListResponse.ProductSummary.builder()
                        .productId(entity.getId().toString())
                        .name(entity.getName())
                        .price(entity.getPrice().toString())
                        .imageUrl(entity.getImageUrl())
                        .active(entity.getActive())
                        .inStock(entity.getStockQuantity() > 0)
                        .build())
                .collect(Collectors.toList());

        return ProductListResponse.builder()
                .products(productSummaries)
                .currentPage(entityPage.getNumber())
                .totalPages(entityPage.getTotalPages())
                .totalItems(entityPage.getTotalElements())
                .build();
    }
}