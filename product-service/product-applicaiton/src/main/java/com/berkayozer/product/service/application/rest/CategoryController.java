package com.berkayozer.product.service.application.rest;

import com.berkayozer.product.service.domain.dto.create.CreateCategoryCommand;
import com.berkayozer.product.service.domain.dto.create.CreateCategoryResponse;
import com.berkayozer.product.service.domain.dto.update.UpdateCategoryCommand;
import com.berkayozer.product.service.domain.ports.input.service.CategoryApplicationService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@Slf4j
@RestController
@RequestMapping(value = "/api/v1/categories", produces = "application/vnd.api.v1+json")
@Tag(name = "Category API", description = "Category management operations. Requires only ADMIN/SUPER_ADMIN privileges")
public class CategoryController {

    private final CategoryApplicationService categoryApplicationService;

    public CategoryController(CategoryApplicationService categoryApplicationService) {
        this.categoryApplicationService = categoryApplicationService;
    }

    @Operation(summary = "Create a Category", description = "Adds a new category to the system. Requires ADMIN privileges")
    @PostMapping
    @PreAuthorize("hasAuthority('ROLE_ADMIN') or hasAuthority('ROLE_SUPER_ADMIN')")
    public ResponseEntity<CreateCategoryResponse> createCategory(@Valid @RequestBody CreateCategoryCommand command) {
        log.info("Creating category with name: {}", command.getName());
        CreateCategoryResponse response = categoryApplicationService.createCategory(command);
        return ResponseEntity.ok(response);
    }

    @Operation(summary = "Update a Category", description = "Updates information for an existing category. Requires ADMIN privileges.")
    @PutMapping("/{categoryId}")
    @PreAuthorize("hasAuthority('ROLE_ADMIN') or hasAuthority('ROLE_SUPER_ADMIN')")    public ResponseEntity<Void> updateCategory(@PathVariable UUID categoryId,
                                               @Valid @RequestBody UpdateCategoryCommand command) {
        log.info("Updating category details for id: {}", categoryId);
        categoryApplicationService.updateCategory(categoryId, command);
        return ResponseEntity.ok().build();
    }
}