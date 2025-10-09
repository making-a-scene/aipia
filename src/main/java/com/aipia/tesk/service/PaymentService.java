package com.aipia.tesk.service;

import com.aipia.tesk.model.Order;
import com.aipia.tesk.model.Payment;
import com.aipia.tesk.exception.OrderNotFoundException;
import com.aipia.tesk.repository.OrderRepository;
import com.aipia.tesk.repository.PaymentRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class PaymentService {

    private final PaymentRepository paymentRepository;
    private final OrderRepository orderRepository;

    @Transactional
    public Payment createPayment(Long orderId) {
        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new OrderNotFoundException("주문을 찾을 수 없습니다."));

        Payment payment = Payment.createPayment(order);
        return paymentRepository.save(payment);
    }

    @Transactional
    public void performPayment(Long paymentId) {
        Payment payment = paymentRepository.findById(paymentId)
                .orElseThrow(() -> new IllegalArgumentException("결제를 찾을 수 없습니다."));

        payment.performPayment();
    }
}
