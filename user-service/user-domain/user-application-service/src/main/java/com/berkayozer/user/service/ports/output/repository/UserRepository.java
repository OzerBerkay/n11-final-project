package com.berkayozer.user.service.ports.output.repository;

import com.berkayozer.user.service.domain.entity.User;

import java.util.Optional;
import java.util.UUID;

public interface UserRepository {
    User save(User user);
    Optional<User> findByEmail(String email);
    Optional<User> findById(UUID id); //for admin op
}