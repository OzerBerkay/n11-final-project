package com.berkayozer.order.service.domain;

import com.berkayozer.order.service.domain.dto.message.ProductResponse;
import com.berkayozer.order.service.domain.ports.input.message.listener.ProductResponseMessageListener;
import com.berkayozer.order.service.domain.saga.OrderProductSaga;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class ProductResponseMessageListenerImpl implements ProductResponseMessageListener {

    private final OrderProductSaga orderProductSaga;

    @Override
    public void productReserved(ProductResponse productResponse) {
        log.info("ProductReservedEvent received for order id: {}", productResponse.getOrderId());
        orderProductSaga.process(productResponse);
    }

    @Override
    public void productRejected(ProductResponse productResponse) {
        log.info("ProductRejectedEvent received for order id: {} with failure messages: {}",
                productResponse.getOrderId(), String.join(",", productResponse.getFailureMessages()));
        orderProductSaga.rollback(productResponse);
    }
}