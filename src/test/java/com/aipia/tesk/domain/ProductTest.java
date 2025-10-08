package com.aipia.tesk.domain;

import com.aipia.tesk.exception.InvalidProductException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.*;

class ProductTest {

    @Test
    @DisplayName("상품 정상 등록 테스트")
    void createProductSuccess() {
        // given
        String name = "테스트 상품";
        int price = 10000;
        int stock = 100;

        // when
        Product product = Product.createProduct(name, price, stock);

        // then
        assertThat(product).isNotNull();
        assertThat(product.getName()).isEqualTo("테스트 상품");
        assertThat(product.getPrice()).isEqualTo(10000);
        assertThat(product.getStock()).isEqualTo(100);
    }

    @Test
    @DisplayName("상품 등록 시 상품명이 20자를 초과하면 예외 발생")
    void createProductWithNameExceeding20Characters() {
        // given
        String longName = "이것은스물한글자를초과하는상품명입니다";
        int price = 10000;
        int stock = 100;

        // when & then
        assertThatThrownBy(() -> Product.createProduct(longName, price, stock))
                .isInstanceOf(InvalidProductException.class)
                .hasMessage("상품명은 20자를 초과할 수 없습니다.");
    }

    @Test
    @DisplayName("상품 등록 시 가격이 1억원을 초과하면 예외 발생")
    void createProductWithPriceExceeding100Million() {
        // given
        String name = "테스트 상품";
        int price = 100_000_001;
        int stock = 100;

        // when & then
        assertThatThrownBy(() -> Product.createProduct(name, price, stock))
                .isInstanceOf(InvalidProductException.class)
                .hasMessage("상품 가격은 1억원을 초과할 수 없습니다.");
    }

    @Test
    @DisplayName("상품 재고 수정 시 재고가 음수가 되면 예외 발생")
    void updateStockResultingInNegative() {
        // given
        Product product = Product.createProduct("테스트 상품", 10000, 10);

        // when & then
        assertThatThrownBy(() -> product.updateStock(-11))
                .isInstanceOf(InvalidProductException.class)
                .hasMessage("재고는 음수가 될 수 없습니다.");
    }
}
