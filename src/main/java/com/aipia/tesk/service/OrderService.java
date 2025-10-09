package com.aipia.tesk.service;

import com.aipia.tesk.model.Member;
import com.aipia.tesk.model.Order;
import com.aipia.tesk.dto.OrderCreateDto;
import com.aipia.tesk.exception.InvalidDateRangeException;
import com.aipia.tesk.exception.MemberNotFoundException;
import com.aipia.tesk.exception.OrderNotFoundException;
import com.aipia.tesk.repository.MemberRepository;
import com.aipia.tesk.repository.OrderRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class OrderService {

    private final OrderRepository orderRepository;
    private final MemberRepository memberRepository;
    private final OrderProductService orderProductService;

    private static final LocalDate MIN_ORDER_DATE = LocalDate.of(2020, 1, 1);

    @Transactional
    public Order createOrder(OrderCreateDto dto) {
        Member member = memberRepository.findById(dto.getMemberId())
                .orElseThrow(() -> new MemberNotFoundException("회원을 찾을 수 없습니다."));

        Order order = Order.builder()
                .member(member)
                .orderedAt(LocalDateTime.now())
                .build();

        Order savedOrder = orderRepository.save(order);

        // OrderProduct 생성 (재고 검증 포함, 실패 시 트랜잭션 롤백)
        orderProductService.createOrderProducts(savedOrder, dto.getOrderProducts());

        return savedOrder;
    }

    public Order findOrderById(Long orderId) {
        return orderRepository.findById(orderId)
                .orElseThrow(() -> new OrderNotFoundException("주문을 찾을 수 없습니다."));
    }

    public Page<Order> findOrdersByMemberAndDateRange(Long memberId, LocalDate startDate, Pageable pageable) {
        // 날짜 범위 검증
        if (startDate != null) {
            validateDateRange(startDate);
            LocalDateTime startDateTime = startDate.atStartOfDay();
            return orderRepository.findByMemberIdAndOrderedAtAfter(memberId, startDateTime, pageable);
        }

        // 날짜 미지정 시 전체 조회
        return orderRepository.findByMemberId(memberId, pageable);
    }

    private void validateDateRange(LocalDate startDate) {
        if (startDate.isBefore(MIN_ORDER_DATE) || startDate.isAfter(LocalDate.now())) {
            throw new InvalidDateRangeException("조회 가능한 기간은 2020년 1월 1일부터 오늘까지입니다.");
        }
    }
}
