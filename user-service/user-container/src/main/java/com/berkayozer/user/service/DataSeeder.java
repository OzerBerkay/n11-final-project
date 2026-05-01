package com.berkayozer.user.service;

import com.berkayozer.user.service.dataaccess.user.entity.UserEntity;
import com.berkayozer.user.service.dataaccess.user.repository.UserJpaRepository;
import com.berkayozer.user.service.domain.valueobject.AccountStatus;
import com.berkayozer.user.service.domain.valueobject.UserRole;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.util.UUID;

@Slf4j
@Component
@RequiredArgsConstructor
public class DataSeeder implements CommandLineRunner {

    private final UserJpaRepository userJpaRepository;
    private final PasswordEncoder passwordEncoder; //It will be automatically injected from the security configuration

    @Override
    public void run(String... args) {
        String adminEmail = "admin@n11.com";

        if (userJpaRepository.findByEmail(adminEmail).isEmpty()) {
            UserEntity adminUser = UserEntity.builder()
                    .id(UUID.randomUUID())
                    .firstName("Super")
                    .lastName("Admin")
                    .email(adminEmail)
                    .password(passwordEncoder.encode("admin123"))
                    .accountStatus(AccountStatus.ACTIVE)
                    .role(UserRole.ROLE_SUPER_ADMIN)
                    .address("N11 HQ, Istanbul")
                    .build();

            userJpaRepository.save(adminUser);
            log.info("Default ADMIN user created. Email: admin@n11.com | Password: admin123");
        }
    }
}