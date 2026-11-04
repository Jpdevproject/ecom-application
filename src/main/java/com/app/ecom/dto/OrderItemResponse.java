package com.app.ecom.dto;

import lombok.*;

import java.math.BigDecimal;
@Data
@Builder
@AllArgsConstructor
@Getter
@NoArgsConstructor
@Setter
public class OrderItemResponse {
    private Long id;
    private Long productId;
    private Integer quantity;
    private BigDecimal price;
    private BigDecimal subTotal;

}
