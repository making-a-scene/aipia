package com.aipia.tesk.controller;

import com.aipia.tesk.domain.Order;
import com.aipia.tesk.dto.OrderCreateDto;
import com.aipia.tesk.dto.OrderResponseDto;
import com.aipia.tesk.service.OrderService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;

@RestController
@RequestMapping("/api/orders")
@RequiredArgsConstructor
public class OrderController {

    private final OrderService orderService;

    @PostMapping
    public ResponseEntity<OrderResponseDto> createOrder(@RequestBody OrderCreateDto dto) {
        Order order = orderService.createOrder(dto);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(OrderResponseDto.from(order));
    }

    @GetMapping("/{orderId}")
    public ResponseEntity<OrderResponseDto> getOrder(@PathVariable Long orderId) {
        Order order = orderService.findOrderById(orderId);
        return ResponseEntity.ok(OrderResponseDto.from(order));
    }

    @GetMapping
    public ResponseEntity<Page<OrderResponseDto>> getOrders(
            @RequestParam Long memberId,
            @RequestParam(required = false) LocalDate startDate,
            @PageableDefault(size = 10) Pageable pageable) {
        Page<Order> orders = orderService.findOrdersByMemberAndDateRange(memberId, startDate, pageable);
        Page<OrderResponseDto> response = orders.map(OrderResponseDto::from);
        return ResponseEntity.ok(response);
    }
}
