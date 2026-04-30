package com.berkayozer.user.service.domain.event;

import com.berkayozer.domain.event.DomainEvent;
import com.berkayozer.user.service.domain.entity.User;
import java.time.ZonedDateTime;

public class UserCreatedEvent implements DomainEvent<User> {
    private final User user;
    private final ZonedDateTime createdAt;

    public UserCreatedEvent(User user, ZonedDateTime createdAt) {
        this.user = user;
        this.createdAt = createdAt;
    }

    public User getUser() { return user; }
    public ZonedDateTime getCreatedAt() { return createdAt; }
}