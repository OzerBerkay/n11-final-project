package com.berkayozer.user.service.handler;

import com.berkayozer.user.service.domain.entity.User;
import com.berkayozer.user.service.dto.create.CreateUserCommand;
import com.berkayozer.user.service.dto.create.CreateUserResponse;
import com.berkayozer.user.service.handler.helper.UserCreateHelper;
import com.berkayozer.user.service.mapper.UserDataMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class UserCreateCommandHandler {

    private final UserCreateHelper userCreateHelper;
    private final UserDataMapper userDataMapper;

    public CreateUserResponse createUser(CreateUserCommand command) {
        User savedUser = userCreateHelper.persistUser(command);
        log.info("User creation transaction completed for email: {}", command.getEmail());
        return userDataMapper.userToCreateUserResponse(savedUser, "User registered successfully!");
    }
}