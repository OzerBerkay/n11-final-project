package com.berkayozer.product.service.domain.handler;

import com.berkayozer.product.service.domain.dto.create.CreateCategoryCommand;
import com.berkayozer.product.service.domain.dto.create.CreateCategoryResponse;
import com.berkayozer.product.service.domain.entity.Category;
import com.berkayozer.product.service.domain.exception.ProductDomainException;
import com.berkayozer.product.service.domain.mapper.CategoryDataMapper;
import com.berkayozer.product.service.domain.ports.output.repository.CategoryRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Slf4j
@Component
public class CreateCategoryCommandHandler {

    private final CategoryRepository categoryRepository;
    private final CategoryDataMapper categoryDataMapper;

    public CreateCategoryCommandHandler(CategoryRepository categoryRepository, CategoryDataMapper categoryDataMapper) {
        this.categoryRepository = categoryRepository;
        this.categoryDataMapper = categoryDataMapper;
    }

    @Transactional
    public CreateCategoryResponse createCategory(CreateCategoryCommand command) {
        // BUSINESS RULE CHECK: Is there already a category with the same name? (Case-Insensitive)
        Optional<Category> existingCategory = categoryRepository.findByName(command.getName());
        if (existingCategory.isPresent()) {
            log.warn("Category with name {} already exists!", command.getName());
            throw new ProductDomainException("Category with name '" + command.getName() + "' already exists!");
        }

        Category category = categoryDataMapper.createCategoryCommandToCategory(command);
        category.initializeCategory(); // assign an ID and set active=true.

        Category savedCategory = categoryRepository.save(category);
        if (savedCategory == null) {
            log.error("Could not save category with name: {}", category.getName());
            throw new ProductDomainException("Could not save category!");
        }

        log.info("Category is created with id: {}", savedCategory.getId().getValue());
        return categoryDataMapper.categoryToCreateCategoryResponse(savedCategory);
    }
}