package com.aipia.tesk;

import com.aipia.tesk.domain.Order;
import com.aipia.tesk.domain.Payment;
import com.aipia.tesk.exception.AlreadyPaidException;
import com.aipia.tesk.exception.DuplicatePaymentException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.*;

class PaymentTest {

    private Order testOrder;

    @BeforeEach
    void setUp() {
        testOrder = Order.builder()
                .id(1L)
                .payment(null)
                .build();
    }

    @Test
    @DisplayName("Payment 생성 성공 - Order.payment가 null인 경우")
    void createPaymentSuccess() {
        // given
        // Order의 payment가 null인 상태

        // when
        Payment payment = Payment.createPayment(testOrder);

        // then
        assertThat(payment).isNotNull();
        assertThat(payment.getOrder()).isEqualTo(testOrder);
        assertThat(payment.isPaid()).isFalse();
    }

    @Test
    @DisplayName("Payment 생성 실패 - Order.payment가 이미 존재하는 경우")
    void createPaymentFailWhenPaymentAlreadyExists() {
        // given
        Payment existingPayment = Payment.builder()
                .id(1L)
                .order(testOrder)
                .isPaid(false)
                .build();

        Order orderWithPayment = Order.builder()
                .id(2L)
                .payment(existingPayment)
                .build();

        // when & then
        assertThatThrownBy(() -> Payment.createPayment(orderWithPayment))
                .isInstanceOf(DuplicatePaymentException.class)
                .hasMessage("이미 결제가 존재하는 주문입니다.");
    }

    @Test
    @DisplayName("결제 수행 성공")
    void performPaymentSuccess() {
        // given
        Payment payment = Payment.builder()
                .id(1L)
                .order(testOrder)
                .isPaid(false)
                .build();

        // when
        payment.performPayment();

        // then
        assertThat(payment.isPaid()).isTrue();
    }

    @Test
    @DisplayName("결제 수행 실패 - 이미 결제된 경우")
    void performPaymentFailWhenAlreadyPaid() {
        // given
        Payment payment = Payment.builder()
                .id(1L)
                .order(testOrder)
                .isPaid(true)
                .build();

        // when & then
        assertThatThrownBy(() -> payment.performPayment())
                .isInstanceOf(AlreadyPaidException.class)
                .hasMessage("이미 결제가 완료된 주문입니다.");
    }
}
