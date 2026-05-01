package com.berkayozer.user.service.dto.create;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import java.util.UUID;

@Getter
@Builder
@AllArgsConstructor
public class CreateUserResponse {
    private final UUID userId;
    private final String message;
}