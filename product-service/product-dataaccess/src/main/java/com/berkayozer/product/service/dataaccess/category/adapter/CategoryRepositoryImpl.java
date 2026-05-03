package com.berkayozer.product.service.dataaccess.category.adapter;

import com.berkayozer.product.service.dataaccess.category.mapper.CategoryDataAccessMapper;
import com.berkayozer.product.service.dataaccess.category.repository.CategoryJpaRepository;
import com.berkayozer.product.service.domain.entity.Category;
import com.berkayozer.product.service.domain.ports.output.repository.CategoryRepository;
import com.berkayozer.product.service.domain.valueobject.CategoryId;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
public class CategoryRepositoryImpl implements CategoryRepository {

    private final CategoryJpaRepository categoryJpaRepository;
    private final CategoryDataAccessMapper categoryDataAccessMapper;

    public CategoryRepositoryImpl(CategoryJpaRepository categoryJpaRepository, CategoryDataAccessMapper categoryDataAccessMapper) {
        this.categoryJpaRepository = categoryJpaRepository;
        this.categoryDataAccessMapper = categoryDataAccessMapper;
    }

    @Override
    public Category save(Category category) {
        return categoryDataAccessMapper.categoryEntityToCategory(
                categoryJpaRepository.save(categoryDataAccessMapper.categoryToCategoryEntity(category))
        );
    }

    @Override
    public Optional<Category> findById(CategoryId categoryId) {
        return categoryJpaRepository.findById(categoryId.getValue())
                .map(categoryDataAccessMapper::categoryEntityToCategory);
    }

    @Override
    public Optional<Category> findByName(String name) {
        return categoryJpaRepository.findByNameIgnoreCase(name)
                .map(categoryDataAccessMapper::categoryEntityToCategory);
    }
}