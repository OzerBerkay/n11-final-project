package com.berkayozer.product.service.domain.dto.update;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class UpdateCategoryCommand {
    @NotBlank(message = "Category name is required")
    private String name;

    @NotNull(message = "Active status is required")
    private Boolean active;
}