package com.app.ecom.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

import java.math.BigDecimal;
@Data
@Builder
@AllArgsConstructor
public class CartItemResponse {
    private Long productId;
    private Integer quantity;
    private BigDecimal price;
}
