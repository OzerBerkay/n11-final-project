package com.berkayozer.product.service.dataaccess.product.adapter;

import com.berkayozer.product.service.dataaccess.product.entity.ProductEntity;
import com.berkayozer.product.service.domain.dto.read.ProductSearchQuery;
import jakarta.persistence.criteria.Predicate;
import org.springframework.data.jpa.domain.Specification;

import java.util.ArrayList;
import java.util.List;

public class ProductSpecification {

    public static Specification<ProductEntity> getSpec(ProductSearchQuery query) {
        return (root, cq, cb) -> {
            List<Predicate> predicates = new ArrayList<>();

            if (query.getName() != null && !query.getName().trim().isEmpty()) {
                // ILIKE (Case insensitive) araması
                predicates.add(cb.like(cb.lower(root.get("name")), "%" + query.getName().toLowerCase() + "%"));
            }
            if (query.getBrand() != null && !query.getBrand().trim().isEmpty()) {
                predicates.add(cb.equal(cb.lower(root.get("brand")), query.getBrand().toLowerCase()));
            }
            if (query.getModel() != null && !query.getModel().trim().isEmpty()) {
                predicates.add(cb.equal(cb.lower(root.get("model")), query.getModel().toLowerCase()));
            }
            if (query.getColor() != null && !query.getColor().trim().isEmpty()) {
                predicates.add(cb.equal(cb.lower(root.get("color")), query.getColor().toLowerCase()));
            }
            if (query.getCategoryId() != null) {
                predicates.add(cb.equal(root.get("categoryId"), query.getCategoryId()));
            }
            if (query.getActive() != null) {
                predicates.add(cb.equal(root.get("active"), query.getActive()));
            }

            return cb.and(predicates.toArray(new Predicate[0]));
        };
    }
}