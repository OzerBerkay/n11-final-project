package com.berkayozer.user.service.handler;

import com.berkayozer.user.service.domain.entity.User;
import com.berkayozer.user.service.domain.exception.UserDomainException;
import com.berkayozer.user.service.dto.profile.UpdateAddressCommand;
import com.berkayozer.user.service.ports.output.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Component
@RequiredArgsConstructor
public class UpdateAddressCommandHandler {

    private final UserRepository userRepository;

    @Transactional
    public void updateAddress(String email, UpdateAddressCommand command) {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new UserDomainException("User not found!"));

        user.updateAddress(command.getAddress());
        userRepository.save(user);

        log.info("Address updated successfully for user: {}", email);
    }
}