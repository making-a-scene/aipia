package com.aipia.tesk.service;

import com.aipia.tesk.domain.Member;
import com.aipia.tesk.domain.Order;
import com.aipia.tesk.dto.OrderCreateDto;
import com.aipia.tesk.dto.OrderProductDto;
import com.aipia.tesk.exception.InsufficientStockException;
import com.aipia.tesk.exception.InvalidDateRangeException;
import com.aipia.tesk.exception.OrderNotFoundException;
import com.aipia.tesk.repository.OrderRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.Optional;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.never;

@ExtendWith(MockitoExtension.class)
class OrderServiceTest {

    @Mock
    private OrderRepository orderRepository;

    @Mock
    private OrderProductService orderProductService;

    @InjectMocks
    private OrderService orderService;

    private Member testMember;
    private Order testOrder;

    @BeforeEach
    void setUp() {
        testMember = Member.builder()
                .id(1L)
                .name("홍길동")
                .email("hong@example.com")
                .build();

        testOrder = Order.builder()
                .id(1L)
                .member(testMember)
                .orderedAt(LocalDateTime.now())
                .build();
    }

    @Test
    @DisplayName("주문 생성 성공")
    void createOrderSuccess() {
        // given
        OrderProductDto orderProduct1 = OrderProductDto.builder()
                .productId(1L)
                .quantity(10)
                .build();

        OrderProductDto orderProduct2 = OrderProductDto.builder()
                .productId(2L)
                .quantity(5)
                .build();

        OrderCreateDto dto = OrderCreateDto.builder()
                .memberId(1L)
                .orderProducts(Arrays.asList(orderProduct1, orderProduct2))
                .build();

        given(orderRepository.save(any(Order.class))).willReturn(testOrder);

        // when
        Order createdOrder = orderService.createOrder(dto);

        // then
        assertThat(createdOrder).isNotNull();
        assertThat(createdOrder.getMember().getId()).isEqualTo(1L);

        verify(orderRepository).save(any(Order.class));
    }

    @Test
    @DisplayName("주문 생성 실패 - 재고 부족 시 Order 엔티티 생성되지 않음")
    void createOrderFailDueToInsufficientStock() {
        // given
        OrderProductDto orderProduct = OrderProductDto.builder()
                .productId(1L)
                .quantity(150)
                .build();

        OrderCreateDto dto = OrderCreateDto.builder()
                .memberId(1L)
                .orderProducts(Arrays.asList(orderProduct))
                .build();

        given(orderProductService.validateStock(any())).willThrow(new InsufficientStockException("재고가 부족합니다."));

        // when & then
        assertThatThrownBy(() -> orderService.createOrder(dto))
                .isInstanceOf(InsufficientStockException.class)
                .hasMessage("재고가 부족합니다.");

        verify(orderRepository, never()).save(any(Order.class));
    }

    @Test
    @DisplayName("ID로 주문 조회 성공")
    void findOrderByIdSuccess() {
        // given
        given(orderRepository.findById(1L)).willReturn(Optional.of(testOrder));

        // when
        Order foundOrder = orderService.findOrderById(1L);

        // then
        assertThat(foundOrder).isNotNull();
        assertThat(foundOrder.getId()).isEqualTo(1L);

        verify(orderRepository).findById(1L);
    }

    @Test
    @DisplayName("존재하지 않는 ID로 주문 조회 시 예외 발생")
    void findOrderByIdNotFound() {
        // given
        given(orderRepository.findById(999L)).willReturn(Optional.empty());

        // when & then
        assertThatThrownBy(() -> orderService.findOrderById(999L))
                .isInstanceOf(OrderNotFoundException.class)
                .hasMessage("주문을 찾을 수 없습니다.");

        verify(orderRepository).findById(999L);
    }

    @Test
    @DisplayName("특정 회원의 특정 기간 주문 목록 조회 - 페이징")
    void findOrdersByMemberAndDateRange() {
        // given
        Long memberId = 1L;
        LocalDate startDate = LocalDate.of(2024, 1, 1);
        Pageable pageable = PageRequest.of(0, 10);

        given(orderRepository.findByMemberIdAndOrderedAtAfter(eq(memberId), any(LocalDateTime.class), eq(pageable)))
                .willReturn(Page.empty());

        // when
        Page<Order> orders = orderService.findOrdersByMemberAndDateRange(memberId, startDate, pageable);

        // then
        assertThat(orders).isNotNull();

        verify(orderRepository).findByMemberIdAndOrderedAtAfter(eq(memberId), any(LocalDateTime.class), eq(pageable));
    }

    @Test
    @DisplayName("특정 회원의 전체 주문 목록 조회 - 기간 미지정 시")
    void findAllOrdersByMember() {
        // given
        Long memberId = 1L;
        Pageable pageable = PageRequest.of(0, 10);

        given(orderRepository.findByMemberId(memberId, pageable))
                .willReturn(Page.empty());

        // when
        Page<Order> orders = orderService.findOrdersByMemberAndDateRange(memberId, null, pageable);

        // then
        assertThat(orders).isNotNull();

        verify(orderRepository).findByMemberId(memberId, pageable);
    }

    @Test
    @DisplayName("2020년 1월 1일 이전 날짜로 조회 시 예외 발생")
    void findOrdersWithDateBeforeMinimum() {
        // given
        Long memberId = 1L;
        LocalDate invalidDate = LocalDate.of(2019, 12, 31);
        Pageable pageable = PageRequest.of(0, 10);

        // when & then
        assertThatThrownBy(() -> orderService.findOrdersByMemberAndDateRange(memberId, invalidDate, pageable))
                .isInstanceOf(InvalidDateRangeException.class)
                .hasMessage("조회 가능한 기간은 2020년 1월 1일부터 오늘까지입니다.");
    }

    @Test
    @DisplayName("미래 날짜로 조회 시 예외 발생")
    void findOrdersWithFutureDate() {
        // given
        Long memberId = 1L;
        LocalDate futureDate = LocalDate.now().plusDays(1);
        Pageable pageable = PageRequest.of(0, 10);

        // when & then
        assertThatThrownBy(() -> orderService.findOrdersByMemberAndDateRange(memberId, futureDate, pageable))
                .isInstanceOf(InvalidDateRangeException.class)
                .hasMessage("조회 가능한 기간은 2020년 1월 1일부터 오늘까지입니다.");
    }
}