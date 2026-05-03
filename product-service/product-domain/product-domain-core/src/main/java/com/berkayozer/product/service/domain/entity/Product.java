package com.berkayozer.product.service.domain.entity;

import com.berkayozer.domain.entity.AggregateRoot;
import com.berkayozer.domain.valueobject.Money;
import com.berkayozer.domain.valueobject.ProductId;
import com.berkayozer.product.service.domain.exception.ProductDomainException;
import com.berkayozer.product.service.domain.valueobject.CategoryId;
import com.berkayozer.product.service.domain.valueobject.Stock;

import java.util.UUID;

public class Product extends AggregateRoot<ProductId> {
    private String name;
    private String description;
    private Money price;
    private Stock stock;
    private CategoryId categoryId;
    private String brand;
    private String model;
    private String color;
    private String imageUrl;

    private Long version; // Optimistic Locking için
    private boolean active;

    // Method to be called when the product is first created
    public void initializeProduct() {
        setId(new ProductId(UUID.randomUUID()));
        this.active = true;
    }

    // Updating all product information at once (MVP Approach)
    public void updateProductDetails(String name, String description, Money price, Stock stock,
                                     CategoryId categoryId, String brand, String model,
                                     String color, String imageUrl, boolean active) {
        this.name = name;
        this.description = description;

        // We're not disregarding our existing business rules, we're using them!
        updatePrice(price);

        this.stock = stock; // The Stock class's own constructor already handles negative checking.
        this.categoryId = categoryId;
        this.brand = brand;
        this.model = model;
        this.color = color;
        this.imageUrl = imageUrl;
        this.active = active;
    }

    // Price Update Business Rule
    public void updatePrice(Money newPrice) {
        if (newPrice == null || !newPrice.isGreaterThanZero()) {
            throw new ProductDomainException("Price must be greater than zero!");
        }
        this.price = newPrice;
    }

    // Stock Drop (Protected with Pessimistic Lock or Optimistic at the time of ordering)
    public void decreaseStock(int quantity) {
        this.stock = this.stock.decrease(quantity);
    }

    // Stock Increase (Restoring stock from a procurement or canceled order)
    public void increaseStock(int quantity) {
        this.stock = this.stock.increase(quantity);
    }

    // Soft Delete (Deactivating the product)
    public void deactivate() {
        this.active = false;
    }

    private Product(Builder builder) {
        super.setId(builder.productId);
        name = builder.name;
        description = builder.description;
        price = builder.price;
        stock = builder.stock;
        categoryId = builder.categoryId;
        version = builder.version;
        active = builder.active;
        brand = builder.brand;
        model = builder.model;
        color = builder.color;
        imageUrl = builder.imageUrl;
    }

    public String getName() { return name; }
    public String getDescription() { return description; }
    public Money getPrice() { return price; }
    public Stock getStock() { return stock; }
    public CategoryId getCategoryId() { return categoryId; }
    public Long getVersion() { return version; }
    public boolean isActive() { return active; }
    public String getColor() { return color; }
    public String getModel() { return model; }
    public String getBrand() { return brand; }
    public String imageUrl() { return imageUrl; }

    public static final class Builder {
        private ProductId productId;
        private String name;
        private String description;
        private Money price;
        private Stock stock;
        private CategoryId categoryId;
        private Long version;
        private boolean active;
        private String color;
        private String model;
        private String brand;
        private String imageUrl;

        private Builder() {}

        public static Builder builder() { return new Builder(); }

        public Builder id(ProductId val) { productId = val; return this; }
        public Builder name(String val) { name = val; return this; }
        public Builder description(String val) { description = val; return this; }
        public Builder price(Money val) { price = val; return this; }
        public Builder stock(Stock val) { stock = val; return this; }
        public Builder categoryId(CategoryId val) { categoryId = val; return this; }
        public Builder version(Long val) { version = val; return this; }
        public Builder active(boolean val) { active = val; return this; }
        public Builder color(String val) { color = val; return this; }
        public Builder model(String val) { model = val; return this; }
        public Builder brand(String val) { brand = val; return this; }
        public Builder imageUrl(String val) { imageUrl = val; return this; }

        public Product build() { return new Product(this); }
    }
}