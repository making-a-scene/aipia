package com.aipia.tesk.domain;

import com.aipia.tesk.dto.ProductCreateDto;
import com.aipia.tesk.exception.InvalidProductException;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "product")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Product {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "product_id")
    private Long id;

    @Column(name = "product_name")
    private String name;

    @Column(name = "stock")
    private int stock;

    @Column(name = "price")
    private int price;

    public static Product createProduct(ProductCreateDto dto) {
        Product product = new Product();
        product.name = dto.getName();
        product.price = dto.getPrice();
        product.stock = dto.getStock();
        product.validate();
        return product;
    }

    public void updateStock(int quantity) {
        this.stock += quantity;
        if (this.stock < 0) {
            throw new InvalidProductException("재고는 음수가 될 수 없습니다.");
        }
    }

    private void validate() {
        if (name == null || name.length() > 20) {
            throw new InvalidProductException("상품명은 20자를 초과할 수 없습니다.");
        }
        if (price > 100_000_000) {
            throw new InvalidProductException("상품 가격은 1억원을 초과할 수 없습니다.");
        }
    }
}
