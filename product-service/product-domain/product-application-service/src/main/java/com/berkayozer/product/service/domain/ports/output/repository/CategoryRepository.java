package com.berkayozer.product.service.domain.ports.output.repository;

import com.berkayozer.product.service.domain.entity.Category;
import com.berkayozer.product.service.domain.valueobject.CategoryId;

import java.util.Optional;

public interface CategoryRepository {
    Category save(Category category);
    Optional<Category> findById(CategoryId categoryId);
    Optional<Category> findByName(String name);
}