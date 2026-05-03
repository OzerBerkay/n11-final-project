package com.berkayozer.product.service.dataaccess.product.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.persistence.Version;
import lombok.*;

import java.math.BigDecimal;
import java.util.UUID;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "products")
@Entity
public class ProductEntity {

    @Id
    private UUID id;

    private String name;
    private String description;
    private BigDecimal price;
    private Integer stockQuantity;
    private UUID categoryId;
    private String brand;
    private String model;
    private String color;
    private String imageUrl;
    private Boolean active;

    @Version // Optimistic Locking mekanizmasını aktifleştiren can alıcı anotasyon!
    private Long version;
}