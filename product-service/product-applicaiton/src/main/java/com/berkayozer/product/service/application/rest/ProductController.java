package com.berkayozer.product.service.application.rest;

import com.berkayozer.product.service.domain.dto.create.CreateProductCommand;
import com.berkayozer.product.service.domain.dto.create.CreateProductResponse;
import com.berkayozer.product.service.domain.dto.read.ProductDetailResponse;
import com.berkayozer.product.service.domain.dto.read.ProductListResponse;
import com.berkayozer.product.service.domain.dto.read.ProductSearchQuery;
import com.berkayozer.product.service.domain.dto.update.UpdateProductCommand;
import com.berkayozer.product.service.domain.ports.input.service.ProductApplicationService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirements;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@Slf4j
@RestController
@RequestMapping(value = "/api/v1/products", produces = "application/vnd.api.v1+json")
@Tag(name = "Product API", description = "Catalog reading operations (Public) and Product Management (Admin) operations")
public class ProductController {

    private final ProductApplicationService productApplicationService;

    public ProductController(ProductApplicationService productApplicationService) {
        this.productApplicationService = productApplicationService;
    }

    //  CUSTOMER (PUBLIC/AUTHENTICATED) API'S

    @GetMapping
    @SecurityRequirements
    public ResponseEntity<ProductListResponse> getAllProducts(ProductSearchQuery query) {
        log.info("Fetching products with filters - Category: {}, Brand: {}, Page: {}",
                query.getCategoryId(), query.getBrand(), query.getPage());

        ProductListResponse response = productApplicationService.getAllProducts(query);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/{productId}")
    @SecurityRequirements
    public ResponseEntity<ProductDetailResponse> getProductById(@PathVariable UUID productId) {
        log.info("Fetching product details for id: {}", productId);
        ProductDetailResponse response = productApplicationService.getProductById(productId);
        return ResponseEntity.ok(response);
    }

    // ADMIN (CATALOG MANAGEMENT) API'S

    @Operation(summary = "Create a Product", description = "Adds a new product to the system. Requires only ADMIN/SUPER_ADMIN privileges.")
    @PostMapping
    @PreAuthorize("hasAuthority('ROLE_ADMIN') or hasAuthority('ROLE_SUPER_ADMIN')")
    public ResponseEntity<CreateProductResponse> createProduct(@Valid @RequestBody CreateProductCommand createProductCommand) {
        log.info("Creating product with name: {}", createProductCommand.getName());
        CreateProductResponse createProductResponse = productApplicationService.createProduct(createProductCommand);
        log.info("Product created with id: {}", createProductResponse.getProductId());
        return ResponseEntity.ok(createProductResponse);
    }

    @Operation(summary = "Update Product", description = "Updates the details of an existing product. Requires only ADMIN/SUPER_ADMIN privileges.")
    @PutMapping("/{productId}")
    @PreAuthorize("hasAuthority('ROLE_ADMIN') or hasAuthority('ROLE_SUPER_ADMIN')")
    public ResponseEntity<Void> updateProduct(@PathVariable UUID productId,
                                              @Valid @RequestBody UpdateProductCommand command) {
        log.info("Updating product details for id: {}", productId);
        productApplicationService.updateProduct(productId, command);
        return ResponseEntity.ok().build();
    }
}