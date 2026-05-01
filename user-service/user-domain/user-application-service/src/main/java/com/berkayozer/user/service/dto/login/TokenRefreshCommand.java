package com.berkayozer.user.service.dto.login;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
@AllArgsConstructor
public class TokenRefreshCommand {
    @NotBlank(message = "Refresh Token is required")
    private final String refreshToken;
}