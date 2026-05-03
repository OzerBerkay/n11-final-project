package com.berkayozer.order.service.domain.dto.message;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

import java.util.List;

@Getter
@Builder
@AllArgsConstructor
public class ProductResponse {
    private String id;
    private String sagaId;
    private String orderId;
    private String status; // RESERVED, FAILED
    private List<String> failureMessages;
}