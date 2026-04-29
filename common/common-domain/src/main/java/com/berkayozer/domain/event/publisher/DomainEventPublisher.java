package com.berkayozer.domain.event.publisher;

import com.berkayozer.domain.event.DomainEvent;

public interface DomainEventPublisher<T extends DomainEvent> {

    void publish (T domainEvent);
}
