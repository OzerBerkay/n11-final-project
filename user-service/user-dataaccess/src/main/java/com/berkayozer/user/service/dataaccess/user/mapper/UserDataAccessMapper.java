package com.berkayozer.user.service.dataaccess.user.mapper;

import com.berkayozer.domain.valueobject.UserId;
import com.berkayozer.user.service.dataaccess.user.entity.UserEntity;
import com.berkayozer.user.service.domain.entity.User;
import com.berkayozer.user.service.domain.valueobject.Email;
import com.berkayozer.user.service.domain.valueobject.FirstName;
import com.berkayozer.user.service.domain.valueobject.LastName;
import org.springframework.stereotype.Component;

@Component
public class UserDataAccessMapper {

    public UserEntity userToUserEntity(User user) {
        return UserEntity.builder()
                .id(user.getId().getValue())
                .firstName(user.getFirstName().getValue())
                .lastName(user.getLastName().getValue())
                .email(user.getEmail().getValue())
                .password(user.getPassword())
                .accountStatus(user.getAccountStatus())
                .role(user.getRole())
                .address(user.getAddress())
                .build();
    }

    public User userEntityToUser(UserEntity userEntity) {
        return User.Builder.builder()
                .id(new UserId(userEntity.getId()))
                .firstName(new FirstName(userEntity.getFirstName()))
                .lastName(new LastName(userEntity.getLastName()))
                .email(new Email(userEntity.getEmail()))
                .password(userEntity.getPassword())
                .accountStatus(userEntity.getAccountStatus())
                .role(userEntity.getRole())
                .address(userEntity.getAddress())
                .build();
    }
}