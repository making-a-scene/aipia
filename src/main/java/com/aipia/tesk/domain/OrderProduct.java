package com.aipia.tesk.domain;

import com.aipia.tesk.exception.InsufficientStockException;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "order_product")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
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

    public static void validateStock(Product product, int quantity) {
        if (product.getStock() < quantity) {
            throw new InsufficientStockException("재고가 부족합니다.");
        }
    }

    public static OrderProduct createOrderProduct(Order order, Product product, int quantity) {
        validateStock(product, quantity);

        OrderProduct orderProduct = new OrderProduct();
        orderProduct.order = order;
        orderProduct.product = product;
        orderProduct.quantity = quantity;

        return orderProduct;
    }
}
