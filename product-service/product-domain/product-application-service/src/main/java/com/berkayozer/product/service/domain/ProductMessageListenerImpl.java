package com.berkayozer.product.service.domain;

import com.berkayozer.product.service.domain.dto.message.ReleaseStockCommand;
import com.berkayozer.product.service.domain.dto.message.ReserveStockCommand;
import com.berkayozer.product.service.domain.handler.ProductStockReleaseCommandHandler;
import com.berkayozer.product.service.domain.handler.ProductStockReserveCommandHandler;
import com.berkayozer.product.service.domain.ports.input.message.listener.ProductMessageListener;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class ProductMessageListenerImpl implements ProductMessageListener {

    private final ProductStockReserveCommandHandler productStockReserveCommandHandler;
    private final ProductStockReleaseCommandHandler productStockReleaseCommandHandler;

    public ProductMessageListenerImpl(ProductStockReserveCommandHandler productStockReserveCommandHandler, ProductStockReleaseCommandHandler productStockReleaseCommandHandler) {
        this.productStockReserveCommandHandler = productStockReserveCommandHandler;
        this.productStockReleaseCommandHandler = productStockReleaseCommandHandler;
    }

    @Override
    public void reserveProductStock(ReserveStockCommand reserveStockCommand) {
        productStockReserveCommandHandler.reserveStock(reserveStockCommand);
    }

    @Override
    public void releaseProductStock(ReleaseStockCommand releaseStockCommand) {
        log.info("Release stock triggered for product: {} via Order: {}", releaseStockCommand.getProductId(), releaseStockCommand.getOrderId());
        productStockReleaseCommandHandler.releaseStock(releaseStockCommand);
    }
}