package com.berkayozer.user.service.domain;

import com.berkayozer.user.service.domain.entity.User;
import com.berkayozer.user.service.domain.event.UserCreatedEvent;

public interface UserDomainService {
    UserCreatedEvent validateAndInitiateUser(User user);
}