package com.berkayozer.user.service.dto.create;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
@AllArgsConstructor
public class CreateUserCommand {
    @NotNull @Size(min = 2, max = 50)
    private final String firstName;

    @NotNull @Size(min = 2, max = 50)
    private final String lastName;

    @NotNull @Email
    private final String email;

    @NotNull @Size(min = 6)
    private final String password;
}