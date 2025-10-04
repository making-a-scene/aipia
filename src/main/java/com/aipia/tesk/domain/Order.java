package com.aipia.tesk.domain;

import jakarta.persistence.*;
import lombok.Getter;

import java.time.LocalDateTime;

@Entity
@Table(name = "order")
@Getter
public class Order {
    @Id @GeneratedValue(strategy = GenerationType.SEQUENCE)
    @Column(name = "order_id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id",  nullable = false)
    private Member member;

    @OneToOne
    @JoinColumn(name = "payment_id",  nullable = false)
    private Payment payment;

    @Column(name = "ordered_at")
    private LocalDateTime orderedAt;
}
