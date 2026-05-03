package com.berkayozer.product.service.domain.handler;

import com.berkayozer.product.service.domain.dto.update.UpdateCategoryCommand;
import com.berkayozer.product.service.domain.entity.Category;
import com.berkayozer.product.service.domain.exception.ProductNotFoundException;
import com.berkayozer.product.service.domain.ports.output.repository.CategoryRepository;
import com.berkayozer.product.service.domain.valueobject.CategoryId;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@Slf4j
@Component
public class UpdateCategoryCommandHandler {

    private final CategoryRepository categoryRepository;

    public UpdateCategoryCommandHandler(CategoryRepository categoryRepository) {
        this.categoryRepository = categoryRepository;
    }

    @Transactional
    public void updateCategory(UUID categoryId, UpdateCategoryCommand command) {
        Category category = categoryRepository.findById(new CategoryId(categoryId))
                .orElseThrow(() -> new ProductNotFoundException("Category not found with id: " + categoryId));

        category.update(command.getName(), command.getActive());

        Category savedCategory = categoryRepository.save(category);
        log.info("Category is updated with id: {} and active status: {}", savedCategory.getId().getValue(), savedCategory.isActive());
    }
}