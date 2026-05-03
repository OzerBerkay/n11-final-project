package com.berkayozer.product.service.dataaccess.category.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.*;

import java.util.UUID;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "categories")
@Entity
public class CategoryEntity {

    @Id
    private UUID id;

    private String name;
    private Boolean active;
}