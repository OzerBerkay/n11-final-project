package com.berkayozer.user.service.handler;

import com.berkayozer.user.service.domain.entity.User;
import com.berkayozer.user.service.domain.exception.UserDomainException;
import com.berkayozer.user.service.dto.profile.GetUserResponse;
import com.berkayozer.user.service.mapper.UserDataMapper;
import com.berkayozer.user.service.ports.output.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
@RequiredArgsConstructor
public class GetUserProfileQueryHandler {

    private final UserRepository userRepository;
    private final UserDataMapper userDataMapper;

    @Transactional(readOnly = true)
    public GetUserResponse getUserProfile(String email) {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new UserDomainException("User not found!"));
        return userDataMapper.userToGetUserResponse(user);
    }
}