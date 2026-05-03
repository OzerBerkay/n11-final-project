package com.berkayozer.product.service.dataaccess.category.mapper;

import com.berkayozer.product.service.dataaccess.category.entity.CategoryEntity;
import com.berkayozer.product.service.domain.entity.Category;
import com.berkayozer.product.service.domain.valueobject.CategoryId;
import org.springframework.stereotype.Component;

@Component
public class CategoryDataAccessMapper {

    public CategoryEntity categoryToCategoryEntity(Category category) {
        return CategoryEntity.builder()
                .id(category.getId().getValue())
                .name(category.getName())
                .active(category.isActive())
                .build();
    }

    public Category categoryEntityToCategory(CategoryEntity categoryEntity) {
        Category category = Category.Builder.builder()
                .name(categoryEntity.getName())
                .build();
        category.setId(new CategoryId(categoryEntity.getId()));
        category.update(categoryEntity.getName(), categoryEntity.getActive());
        return category;
    }
}