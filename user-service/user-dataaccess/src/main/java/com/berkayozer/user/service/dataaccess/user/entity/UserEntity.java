package com.berkayozer.user.service.dataaccess.user.entity;

import com.berkayozer.user.service.domain.valueobject.AccountStatus;
import com.berkayozer.user.service.domain.valueobject.UserRole;
import jakarta.persistence.*;
import lombok.*;

import java.util.UUID;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "users", schema = "user_schema")
public class UserEntity {

    @Id
    private UUID id;

    private String firstName;
    private String lastName;

    @Column(unique = true, nullable = false)
    private String email;

    @Column(nullable = false)
    private String password;

    @Enumerated(EnumType.STRING)
    private AccountStatus accountStatus;

    @Enumerated(EnumType.STRING)
    private UserRole role;

    private String address;
}