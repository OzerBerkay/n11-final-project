package com.berkayozer.product.service.domain.mapper;

import com.berkayozer.product.service.domain.dto.create.CreateCategoryCommand;
import com.berkayozer.product.service.domain.dto.create.CreateCategoryResponse;
import com.berkayozer.product.service.domain.entity.Category;
import org.springframework.stereotype.Component;

@Component
public class CategoryDataMapper {

    public Category createCategoryCommandToCategory(CreateCategoryCommand command) {
        return Category.Builder.builder()
                .name(command.getName())
                .build();
    }

    public CreateCategoryResponse categoryToCreateCategoryResponse(Category category) {
        return CreateCategoryResponse.builder()
                .categoryId(category.getId().getValue())
                .build();
    }
}