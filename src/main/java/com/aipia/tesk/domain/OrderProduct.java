package com.aipia.tesk.domain;

import jakarta.persistence.*;
import lombok.Getter;

@Entity
@Table(name = "order_product")
@Getter
public class OrderProduct {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "order_product_id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "product_id",  nullable = false)
    private Product product;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "order_id", nullable = false)
    private Order order;

    @Column(name = "quantity")
    private int quantity;
}
