package com.berkayozer.product.service.dataaccess.category.repository;

import com.berkayozer.product.service.dataaccess.category.entity.CategoryEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface CategoryJpaRepository extends JpaRepository<CategoryEntity, UUID> {
    Optional<CategoryEntity> findByNameIgnoreCase(String name);
}