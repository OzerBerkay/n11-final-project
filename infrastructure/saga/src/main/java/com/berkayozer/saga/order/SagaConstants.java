package com.berkayozer.saga.order;

public final class SagaConstants {

    private SagaConstants(){}

    // This type allows you to retrieve unprocessed messages from the outbox table, specifically those related to the Order process.
    public static final String ORDER_SAGA_NAME = "OrderProcessingSaga";
}

