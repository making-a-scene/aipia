package com.aipia.tesk.dto;

import com.aipia.tesk.model.Order;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class OrderResponseDto {

    private Long orderId;
    private Long memberId;
    private String memberName;
    private List<OrderProductResponseDto> orderProducts;
    private LocalDateTime orderedAt;

    public static OrderResponseDto from(Order order) {
        return OrderResponseDto.builder()
                .orderId(order.getId())
                .memberId(order.getMember().getId())
                .memberName(order.getMember().getName())
                .orderProducts(order.getOrderedProducts().stream()
                        .map(OrderProductResponseDto::from)
                        .collect(Collectors.toList()))
                .orderedAt(order.getOrderedAt())
                .build();
    }
}