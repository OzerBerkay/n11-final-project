package com.berkayozer.cart.service.domain.entity;

import com.berkayozer.domain.entity.AggregateRoot;
import com.berkayozer.domain.valueobject.UserId;
import com.berkayozer.domain.valueobject.ProductId;
import com.berkayozer.domain.valueobject.Money;
import com.berkayozer.cart.service.domain.valueobject.CartId;
import com.berkayozer.cart.service.domain.valueobject.CartItemId;
import com.berkayozer.cart.service.domain.exception.CartDomainException;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;

public class Cart extends AggregateRoot<CartId> {
    private final UserId userId;
    private List<CartItem> items;
    private Money totalAmount;

    private Cart(Builder builder) {
        super.setId(builder.cartId);
        userId = builder.userId;
        items = builder.items != null ? builder.items : new ArrayList<>();
        totalAmount = builder.totalAmount != null ? builder.totalAmount : Money.ZERO;
    }

    // Yeni sepet oluşumu
    public void initializeCart() {
        setId(new CartId(UUID.randomUUID()));
        this.items = new ArrayList<>();
        this.totalAmount = Money.ZERO;
    }

    // Sepete ürün ekler veya olanın miktarını değiştirir
    public void addItem(CartItem cartItem) {
        // 1. Ürün zaten sepetimizde var mı?
        Optional<CartItem> existingItem = items.stream()
                .filter(item -> item.getProductId().equals(cartItem.getProductId()))
                .findFirst();

        if (existingItem.isPresent()) {
            // GÜNCELLEME SENARYOSU: Sepette ürün var, miktarını topluyoruz.
            CartItem foundItem = existingItem.get();
            foundItem.addQuantity(cartItem.getQuantity());

            // Eğer miktar 0 veya altına düştüyse sepetten uçuruyoruz.
            if (foundItem.getQuantity() <= 0) {
                items.remove(foundItem);
            }
        } else {
            // YENİ EKLEME SENARYOSU: Sepette yoksa, gelen miktar eksi olamaz!
            if (cartItem.getQuantity() <= 0) {
                throw new CartDomainException("New items must have a quantity greater than zero!");
            }

            long nextId = items.size() + 1;
            cartItem.initializeCartItem(super.getId(), new CartItemId(nextId));
            items.add(cartItem);
        }
        calculateTotalAmount();
    }

    // Sepetten ürün çıkarır
    public void removeItem(ProductId productId) {
        items.removeIf(item -> item.getProductId().equals(productId));
        calculateTotalAmount();
    }

    // Sepeti boşaltır
    public void clearCart() {
        items.clear();
        totalAmount = Money.ZERO;
    }

    // Dışarıdan güncel fiyatlar map'i gelir, sepet kendini günceller
    public void updatePricesAndCalculateTotal(Map<ProductId, Money> currentPrices) {
        for (CartItem item : items) {
            if (currentPrices.containsKey(item.getProductId())) {
                item.updatePrice(currentPrices.get(item.getProductId()));
            } else {
                // Eğer ürün veritabanından tamamen silinmişse (Product Service'den gelmemişse)
                // sepette tutmanın mantığı yok, veya fiyatı 0'lanabilir. Şimdilik hata atmıyoruz.
            }
        }
        calculateTotalAmount();
    }

    private void calculateTotalAmount() {
        this.totalAmount = items.stream()
                .map(CartItem::getSubTotal)
                .reduce(Money.ZERO, Money::add);
    }

    public UserId getUserId() { return userId; }
    public List<CartItem> getItems() { return items; }
    public Money getTotalAmount() { return totalAmount; }

    public static Builder builder() { return new Builder(); }

    public static final class Builder {
        private CartId cartId;
        private UserId userId;
        private List<CartItem> items;
        private Money totalAmount;

        private Builder() {}

        public Builder cartId(CartId val) { cartId = val; return this; }
        public Builder userId(UserId val) { userId = val; return this; }
        public Builder items(List<CartItem> val) { items = val; return this; }
        public Builder totalAmount(Money val) { totalAmount = val; return this; }
        public Cart build() { return new Cart(this); }
    }
}