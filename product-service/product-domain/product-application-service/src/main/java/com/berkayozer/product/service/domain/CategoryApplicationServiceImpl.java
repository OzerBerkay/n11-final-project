package com.berkayozer.product.service.domain;

import com.berkayozer.product.service.domain.dto.create.CreateCategoryCommand;
import com.berkayozer.product.service.domain.dto.create.CreateCategoryResponse;
import com.berkayozer.product.service.domain.dto.update.UpdateCategoryCommand;
import com.berkayozer.product.service.domain.handler.CreateCategoryCommandHandler;
import com.berkayozer.product.service.domain.handler.UpdateCategoryCommandHandler;
import com.berkayozer.product.service.domain.ports.input.service.CategoryApplicationService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;

import java.util.UUID;

@Slf4j
@Validated
@Service
public class CategoryApplicationServiceImpl implements CategoryApplicationService {

    private final CreateCategoryCommandHandler createCategoryCommandHandler;
    private final UpdateCategoryCommandHandler updateCategoryCommandHandler;

    public CategoryApplicationServiceImpl(CreateCategoryCommandHandler createCategoryCommandHandler, UpdateCategoryCommandHandler updateCategoryCommandHandler) {
        this.createCategoryCommandHandler = createCategoryCommandHandler;
        this.updateCategoryCommandHandler = updateCategoryCommandHandler;
    }

    @Override
    public CreateCategoryResponse createCategory(CreateCategoryCommand command) {
        return createCategoryCommandHandler.createCategory(command);
    }

    @Override
    public void updateCategory(UUID categoryId, UpdateCategoryCommand command) {
        updateCategoryCommandHandler.updateCategory(categoryId, command);
    }
}
