package com.berkayozer.order.service.domain;

import com.berkayozer.order.service.domain.entity.Order;
import com.berkayozer.order.service.domain.event.OrderCancelledEvent;
import com.berkayozer.order.service.domain.event.OrderCreatedEvent;
import com.berkayozer.order.service.domain.event.OrderPaidEvent;

import java.util.List;

public interface OrderDomainService {

    // Siparişi başlatır, doğrular ve Product servisi için rezervasyon eventini döner
    OrderCreatedEvent validateAndInitiateOrder(Order order);

    // Siparişin ödemesi alındığında durumu günceller ve event döner
    OrderPaidEvent payOrder(Order order);

    // Siparişin stokları kesinleştiğinde tamamlandı (COMPLETED) durumuna çeker
    void approveOrder(Order order);

    // Herhangi bir adımda hata olursa siparişi reddeder ve iptal eventini döner
    OrderCancelledEvent cancelOrderPayment(Order order, List<String> failureMessages);
}