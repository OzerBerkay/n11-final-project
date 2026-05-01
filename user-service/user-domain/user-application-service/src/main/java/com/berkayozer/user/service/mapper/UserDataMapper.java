package com.berkayozer.user.service.mapper;

import com.berkayozer.user.service.domain.entity.User;
import com.berkayozer.user.service.domain.valueobject.Email;
import com.berkayozer.user.service.domain.valueobject.FirstName;
import com.berkayozer.user.service.domain.valueobject.LastName;
import com.berkayozer.user.service.dto.create.CreateUserCommand;
import com.berkayozer.user.service.dto.create.CreateUserResponse;
import com.berkayozer.user.service.dto.profile.GetUserResponse;
import org.springframework.stereotype.Component;

@Component
public class UserDataMapper {

    public User createUserCommandToUser(CreateUserCommand command) {
        return User.Builder.builder()
                .firstName(new FirstName(command.getFirstName()))
                .lastName(new LastName(command.getLastName()))
                .email(new Email(command.getEmail()))
                // We don't take the raw password from the DTO and send it to the Entity
                // The password field will be left blank; it will be hashed and printed in the Helper
                .build();
    }

    public CreateUserResponse userToCreateUserResponse(User user, String message) {
        return CreateUserResponse.builder()
                .userId(user.getId().getValue())
                .message(message)
                .build();
    }

    public GetUserResponse userToGetUserResponse(User user) {
        return GetUserResponse.builder()
                .id(user.getId().getValue())
                .firstName(user.getFirstName().getValue())
                .lastName(user.getLastName().getValue())
                .email(user.getEmail().getValue())
                .address(user.getAddress())
                .role(user.getRole().name())
                .build();
    }
}