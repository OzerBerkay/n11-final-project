package com.berkayozer.outbox;

public interface OutboxScheduler {

    void processOutboxMessage();
}
