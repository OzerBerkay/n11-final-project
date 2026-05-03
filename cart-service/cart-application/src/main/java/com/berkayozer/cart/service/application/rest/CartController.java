package com.berkayozer.cart.service.application.rest;

import com.berkayozer.cart.service.domain.dto.AddToCartCommand;
import com.berkayozer.cart.service.domain.dto.CartResponse;
import com.berkayozer.cart.service.domain.ports.input.service.CartApplicationService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@Slf4j
@RestController
@RequestMapping(value = "/api/v1/carts", produces = "application/vnd.api.v1+json")
@RequiredArgsConstructor
public class CartController {

    private final CartApplicationService cartApplicationService;

    // Sepeti Getir (Sepetteki fiyatlar bu metod çağrıldığında otomatik güncellenir)
    @GetMapping
    public ResponseEntity<CartResponse> getCart(@AuthenticationPrincipal String userId) {
        log.info("Retrieving cart for user: {}", userId);
        CartResponse cartResponse = cartApplicationService.getCart(UUID.fromString(userId));
        return ResponseEntity.ok(cartResponse);
    }

    // Sepete Ürün Ekle (Veya olan ürünün adedini artır)
    @PostMapping
    public ResponseEntity<CartResponse> addToCart(@AuthenticationPrincipal String userId, @RequestBody AddToCartCommand command) {
        command.setUserId(UUID.fromString(userId));
        log.info("Adding product: {} to cart for user: {}", command.getProductId(), command.getUserId());
        CartResponse cartResponse = cartApplicationService.addToCart(command);
        return ResponseEntity.ok(cartResponse);
    }

    //  Sepetten Ürün Çıkar
    @DeleteMapping("/{userId}/items/{productId}")
    public ResponseEntity<CartResponse> removeFromCart(@PathVariable UUID userId, @PathVariable UUID productId) {
        log.info("Removing product: {} from cart for user: {}", productId, userId);
        CartResponse cartResponse = cartApplicationService.removeFromCart(userId, productId);
        return ResponseEntity.ok(cartResponse);
    }

    // Sepeti Tamamen Boşalt (Checkout/Sipariş başarılı olduğunda veya kullanıcı isterse)
    @DeleteMapping
    public ResponseEntity<Void> clearCart(@AuthenticationPrincipal String userId) {
        log.info("Clearing cart for user: {}", userId);
        cartApplicationService.clearCart(UUID.fromString(userId));
        return ResponseEntity.noContent().build();
    }
}