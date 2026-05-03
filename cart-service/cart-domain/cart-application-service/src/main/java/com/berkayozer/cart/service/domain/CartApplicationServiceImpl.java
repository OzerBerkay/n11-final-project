package com.berkayozer.cart.service.domain;

import com.berkayozer.cart.service.domain.dto.AddToCartCommand;
import com.berkayozer.cart.service.domain.dto.CartResponse;
import com.berkayozer.cart.service.domain.entity.Cart;
import com.berkayozer.cart.service.domain.entity.CartItem;
import com.berkayozer.cart.service.domain.mapper.CartDataMapper;
import com.berkayozer.cart.service.domain.ports.input.service.CartApplicationService;
import com.berkayozer.cart.service.domain.ports.output.remote.ProductGateway;
import com.berkayozer.cart.service.domain.ports.output.repository.CartRepository;
import com.berkayozer.domain.valueobject.Money;
import com.berkayozer.domain.valueobject.ProductId;
import com.berkayozer.domain.valueobject.UserId;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;

import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.stream.Collectors;

@Slf4j
@Validated
@Service
@RequiredArgsConstructor
public class CartApplicationServiceImpl implements CartApplicationService {

    private final CartDomainService cartDomainService;
    private final CartRepository cartRepository;
    private final ProductGateway productGateway;
    private final CartDataMapper cartDataMapper;

    @Override
    public CartResponse getCart(UUID userId) {
        Cart cart = getOrCreateCart(userId);

        // Sepette ürün varsa Product Service'e gidip anlık fiyatları çekiyoruz!
        if (!cart.getItems().isEmpty()) {
            List<ProductId> productIds = cart.getItems().stream()
                    .map(CartItem::getProductId)
                    .collect(Collectors.toList());

            Map<ProductId, Money> currentPrices = productGateway.getCurrentPrices(productIds);

            // Domain service sepet içindeki fiyatları güncelliyor ve totali yeniden hesaplıyor
            cartDomainService.updateCartPrices(cart, currentPrices);

            // Güncel haliyle Redis'e tekrar kaydediyoruz
            cart = cartRepository.save(cart);
        }

        return cartDataMapper.cartToCartResponse(cart);
    }

    @Override
    public CartResponse addToCart(AddToCartCommand command) {
        Cart cart = getOrCreateCart(command.getUserId());
        ProductId productId = new ProductId(command.getProductId());

        // Eklenecek ürünün güncel fiyatını Product Service'den çekiyoruz
        Money currentPrice = productGateway.getProductPrice(productId);

        CartItem newItem = CartItem.builder()
                .productId(productId)
                .quantity(command.getQuantity())
                .price(currentPrice)
                .subTotal(currentPrice.multiply(command.getQuantity()))
                .build();

        cart.addItem(newItem);
        Cart savedCart = cartRepository.save(cart);

        log.info("Product {} added to cart of user {}", command.getProductId(), command.getUserId());
        return cartDataMapper.cartToCartResponse(savedCart);
    }

    @Override
    public CartResponse removeFromCart(UUID userId, UUID productId) {
        Cart cart = getOrCreateCart(userId);
        cart.removeItem(new ProductId(productId));

        Cart savedCart = cartRepository.save(cart);
        log.info("Product {} removed from cart of user {}", productId, userId);

        return cartDataMapper.cartToCartResponse(savedCart);
    }

    @Override
    public void clearCart(UUID userId) {
        cartRepository.deleteByUserId(userId);
        log.info("Cart cleared for user {}", userId);
    }

    // Yardımcı metod: Sepet yoksa yeni yarat
    private Cart getOrCreateCart(UUID userId) {
        return cartRepository.findByUserId(userId).orElseGet(() -> {
            Cart newCart = Cart.builder()
                    .userId(new UserId(userId))
                    .build();
            cartDomainService.initializeNewCart(newCart);
            return newCart;
        });
    }
}