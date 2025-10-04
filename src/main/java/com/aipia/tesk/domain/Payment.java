package com.aipia.tesk.domain;

import jakarta.persistence.*;
import lombok.Getter;

@Entity
@Table(name = "payment")
@Getter
public class Payment {
    @Id @GeneratedValue(strategy = GenerationType.SEQUENCE)
    @Column(name = "payment_id")
    private Long id;

    @OneToOne
    private Order order;

    private boolean isPaid;
}
