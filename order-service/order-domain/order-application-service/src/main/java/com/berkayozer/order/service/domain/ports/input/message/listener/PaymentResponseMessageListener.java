package com.berkayozer.order.service.domain.ports.input.message.listener;

import com.berkayozer.order.service.domain.dto.message.PaymentResponse;

public interface PaymentResponseMessageListener {
    void paymentCompleted(PaymentResponse paymentResponse); // Ödeme alındı (Case A)
    void paymentCancelled(PaymentResponse paymentResponse); // Bakiye yetersiz (Case B)
}