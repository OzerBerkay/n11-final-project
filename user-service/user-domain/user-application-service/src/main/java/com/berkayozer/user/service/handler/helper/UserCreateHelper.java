package com.berkayozer.user.service.handler.helper;

import com.berkayozer.user.service.domain.UserDomainService;
import com.berkayozer.user.service.domain.entity.User;
import com.berkayozer.user.service.domain.event.UserCreatedEvent;
import com.berkayozer.user.service.domain.exception.UserDomainException;
import com.berkayozer.user.service.dto.create.CreateUserCommand;
import com.berkayozer.user.service.mapper.UserDataMapper;
import com.berkayozer.user.service.ports.output.repository.IdentityProviderPort;
import com.berkayozer.user.service.ports.output.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Component
@RequiredArgsConstructor
public class UserCreateHelper {

    private final UserDomainService userDomainService;
    private final UserRepository userRepository;
    private final IdentityProviderPort identityProviderPort;
    private final UserDataMapper userDataMapper;

    @Transactional
    public User persistUser(CreateUserCommand command) {
        // Email uniqueness check
        if (userRepository.findByEmail(command.getEmail()).isPresent()) {
            log.error("User with email: {} already exists!", command.getEmail());
            throw new UserDomainException("User with email " + command.getEmail() + " already exists!");
        }

        // Map Command to Entity (Password still empty)
        User user = userDataMapper.createUserCommandToUser(command);

        // Hash password with BCrypt. We read and hash the password from the Command (DTO), not the User.
        String hashedPassword = identityProviderPort.hashPassword(command.getPassword());
        user.hashPassword(hashedPassword);

        // Domain Logic: Initialize User & Generate Event
        UserCreatedEvent userCreatedEvent = userDomainService.validateAndInitiateUser(user);

        // DB save
        User savedUser = userRepository.save(user);
        log.info("User is saved with id: {}", savedUser.getId().getValue());

        return savedUser;
    }
}