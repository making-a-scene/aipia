package com.aipia.tesk.domain;

import jakarta.persistence.*;

@Entity
@Table(name = "order")
public class Order {
    @Id @GeneratedValue(strategy = GenerationType.SEQUENCE)
    @Column(name = "order_id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id",  nullable = false)
    private Member member;

    @Enumerated(EnumType.STRING)
    private OrderStatus status;
}
