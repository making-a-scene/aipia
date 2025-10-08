package com.aipia.tesk.domain;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

@Entity
@Table(name = "order")
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Order {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "order_id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id",  nullable = false)
    private Member member;

    @OneToMany(mappedBy = "order")
    private List<OrderProduct> orderedProducts;

    @OneToOne
    @JoinColumn(name = "payment_id",  nullable = false)
    private Payment payment;

    @Column(name = "ordered_at")
    private LocalDateTime orderedAt;
}
