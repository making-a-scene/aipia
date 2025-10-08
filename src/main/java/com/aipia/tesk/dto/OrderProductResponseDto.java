package com.aipia.tesk.dto;

import com.aipia.tesk.domain.OrderProduct;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class OrderProductResponseDto {

    private Long productId;
    private String productName;
    private int quantity;
    private int price;

    public static OrderProductResponseDto from(OrderProduct orderProduct) {
        return OrderProductResponseDto.builder()
                .productId(orderProduct.getProduct().getId())
                .productName(orderProduct.getProduct().getName())
                .quantity(orderProduct.getQuantity())
                .price(orderProduct.getProduct().getPrice())
                .build();
    }
}