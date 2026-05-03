package com.berkayozer.product.service.domain.entity;

import com.berkayozer.domain.entity.AggregateRoot;
import com.berkayozer.product.service.domain.valueobject.CategoryId;

public class Category extends AggregateRoot<CategoryId> {
    private String name;
    private boolean active;

    public void initializeCategory() {
        setId(new CategoryId(java.util.UUID.randomUUID()));
        this.active = true;
    }

    public void update(String name, boolean active) {
        changeName(name);
        this.active = active;
    }

    public void changeName(String newName) {
        if (newName == null || newName.trim().isEmpty()) {
            throw new IllegalArgumentException("Category name cannot be empty!");
        }
        this.name = newName;
    }

    public void deactivate() {
        this.active = false;
    }

    private Category(Builder builder) {
        super.setId(builder.categoryId);
        name = builder.name;
        active = builder.active;
    }

    public String getName() { return name; }
    public boolean isActive() { return active; }

    public static final class Builder {
        private CategoryId categoryId;
        private String name;
        private boolean active;

        private Builder() {}

        public static Builder builder() { return new Builder(); }

        public Builder id(CategoryId val) { categoryId = val; return this; }
        public Builder name(String val) { name = val; return this; }
        public Builder active(boolean val) { active = val; return this; }

        public Category build() { return new Category(this); }
    }
}