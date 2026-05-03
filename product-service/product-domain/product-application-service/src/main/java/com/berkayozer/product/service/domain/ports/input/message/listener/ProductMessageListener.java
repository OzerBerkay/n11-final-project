package com.berkayozer.product.service.domain.ports.input.message.listener;

import com.berkayozer.product.service.domain.dto.message.ReleaseStockCommand;
import com.berkayozer.product.service.domain.dto.message.ReserveStockCommand;

public interface ProductMessageListener {
    void reserveProductStock(ReserveStockCommand reserveStockCommand);
    void releaseProductStock(ReleaseStockCommand releaseStockCommand);
}