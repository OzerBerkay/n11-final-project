package com.berkayozer.order.service.domain;

import com.berkayozer.order.service.domain.dto.message.PaymentResponse;
import com.berkayozer.order.service.domain.ports.input.message.listener.PaymentResponseMessageListener;
import com.berkayozer.order.service.domain.saga.OrderPaymentSaga;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class PaymentResponseMessageListenerImpl implements PaymentResponseMessageListener {

    private final OrderPaymentSaga orderPaymentSaga;

    @Override
    public void paymentCompleted(PaymentResponse paymentResponse) {
        log.info("PaymentCompletedEvent received for order id: {}", paymentResponse.getOrderId());
        orderPaymentSaga.process(paymentResponse);
    }

    @Override
    public void paymentCancelled(PaymentResponse paymentResponse) {
        log.info("PaymentCancelledEvent received for order id: {} with failure messages: {}",
                paymentResponse.getOrderId(), String.join(",", paymentResponse.getFailureMessages()));
        orderPaymentSaga.rollback(paymentResponse);
    }
}