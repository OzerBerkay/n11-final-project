package com.berkayozer.product.service.domain.valueobject;

import com.berkayozer.product.service.domain.exception.ProductDomainException;

public class Stock {
    private final int quantity;

    public Stock(int quantity) {
        if (quantity < 0) {
            throw new ProductDomainException("Stock quantity cannot be negative!");
        }
        this.quantity = quantity;
    }

    public int getQuantity() {
        return quantity;
    }

    public boolean hasEnoughStock(int amount) {
        return this.quantity >= amount;
    }

    public Stock decrease(int amount) {
        if (!hasEnoughStock(amount)) {
            throw new ProductDomainException("Not enough stock available!");
        }
        return new Stock(this.quantity - amount);
    }

    public Stock increase(int amount) {
        if (amount < 0) {
            throw new ProductDomainException("Increase amount must be greater than zero!");
        }
        return new Stock(this.quantity + amount);
    }
}