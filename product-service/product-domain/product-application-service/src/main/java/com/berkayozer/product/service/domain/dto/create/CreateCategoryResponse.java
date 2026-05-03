package com.berkayozer.product.service.domain.dto.create;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import java.util.UUID;

@Getter
@Builder
@AllArgsConstructor
public class CreateCategoryResponse {
    private final UUID categoryId;
}