package com.aipia.tesk.domain;

import com.aipia.tesk.exception.AlreadyPaidException;
import com.aipia.tesk.exception.DuplicatePaymentException;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "payment")
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Payment {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "payment_id")
    private Long id;

    @OneToOne
    private Order order;

    private boolean isPaid;

    public static Payment createPayment(Order order) {
        if (order.getPayment() != null) {
            throw new DuplicatePaymentException("이미 결제가 존재하는 주문입니다.");
        }
        Payment payment = new Payment();
        payment.order = order;
        return payment;
    }

    public boolean performPayment() {
        if (this.isPaid) {
            throw new AlreadyPaidException("이미 결제가 완료된 주문입니다.");
        }

        // 결제 수행

        this.isPaid = true;
        return true;
    }
}
