package com.aipia.tesk.domain;

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
}
