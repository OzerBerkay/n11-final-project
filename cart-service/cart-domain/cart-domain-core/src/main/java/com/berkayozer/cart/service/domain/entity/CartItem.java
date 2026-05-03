package com.berkayozer.cart.service.domain.entity;

import com.berkayozer.domain.entity.BaseEntity;
import com.berkayozer.domain.valueobject.Money;
import com.berkayozer.domain.valueobject.ProductId;
import com.berkayozer.cart.service.domain.valueobject.CartId;
import com.berkayozer.cart.service.domain.valueobject.CartItemId;

public class CartItem extends BaseEntity<CartItemId> {
    private CartId cartId;
    private final ProductId productId;
    private int quantity;
    private Money price;
    private Money subTotal;

    private CartItem(Builder builder) {
        super.setId(builder.cartItemId);
        productId = builder.productId;
        quantity = builder.quantity;
        price = builder.price;
        subTotal = builder.subTotal;
    }

    void initializeCartItem(CartId cartId, CartItemId cartItemId) {
        this.cartId = cartId;
        super.setId(cartItemId);
    }

    // Aynı ürün tekrar eklenirse adedi artırır
    void addQuantity(int quantityToAdd) {
        this.quantity += quantityToAdd;
        // Alt toplamı sadece miktar pozitifse güncelle, değilse zaten silinecek
        if (this.quantity > 0) {
            this.subTotal = this.price.multiply(this.quantity);
        }
    }

    // Product servisinden gelen yeni fiyata göre kendini günceller!
    void updatePrice(Money newPrice) {
        this.price = newPrice;
        this.subTotal = this.price.multiply(this.quantity);
    }

    public CartId getCartId() { return cartId; }
    public ProductId getProductId() { return productId; }
    public int getQuantity() { return quantity; }
    public Money getPrice() { return price; }
    public Money getSubTotal() { return subTotal; }

    public static Builder builder() { return new Builder(); }

    public static final class Builder {
        private CartItemId cartItemId;
        private ProductId productId;
        private int quantity;
        private Money price;
        private Money subTotal;

        private Builder() {}

        public Builder cartItemId(CartItemId val) { cartItemId = val; return this; }
        public Builder productId(ProductId val) { productId = val; return this; }
        public Builder quantity(int val) { quantity = val; return this; }
        public Builder price(Money val) { price = val; return this; }
        public Builder subTotal(Money val) { subTotal = val; return this; }
        public CartItem build() { return new CartItem(this); }
    }
}