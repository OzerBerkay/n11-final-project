package com.berkayozer.order.service.domain.ports.input.message.listener;

import com.berkayozer.order.service.domain.dto.message.ProductResponse;

public interface ProductResponseMessageListener {
    void productReserved(ProductResponse productResponse); // Stok ayrıldı (Case A)
    void productRejected(ProductResponse productResponse); // Stok yok (Case B)
}