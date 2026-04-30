package com.berkayozer.user.service.domain;

import com.berkayozer.user.service.domain.entity.User;
import com.berkayozer.user.service.domain.event.UserCreatedEvent;
import com.berkayozer.domain.DomainConstants;

import java.time.ZoneId;
import java.time.ZonedDateTime;

public class UserDomainServiceImpl implements UserDomainService {

    @Override
    public UserCreatedEvent validateAndInitiateUser(User user) {
        // Business rules are executed (e.g., Is the account status active, are the data complete?)
        user.initializeUser();

        // We use DomainConstants.UTC for ZonedDateTime.
        return new UserCreatedEvent(user, ZonedDateTime.now(ZoneId.of(DomainConstants.UTC)));
    }
}