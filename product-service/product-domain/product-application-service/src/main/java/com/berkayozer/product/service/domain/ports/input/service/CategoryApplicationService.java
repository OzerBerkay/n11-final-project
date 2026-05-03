package com.berkayozer.product.service.domain.ports.input.service;

import com.berkayozer.product.service.domain.dto.create.CreateCategoryCommand;
import com.berkayozer.product.service.domain.dto.create.CreateCategoryResponse;
import com.berkayozer.product.service.domain.dto.update.UpdateCategoryCommand;
import java.util.UUID;

public interface CategoryApplicationService {
    CreateCategoryResponse createCategory(CreateCategoryCommand command);
    void updateCategory(UUID categoryId, UpdateCategoryCommand command);
}