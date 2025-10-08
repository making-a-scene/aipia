package com.aipia.tesk.domain;

import com.aipia.tesk.dto.ProductCreateDto;
import com.aipia.tesk.exception.InsufficientStockException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.*;

class OrderProductTest {

    private Order testOrder;
    private Product testProduct;

    @BeforeEach
    void setUp() {
        testOrder = Order.builder()
                .id(1L)
                .build();

        ProductCreateDto dto = ProductCreateDto.builder()
                .name("테스트 상품")
                .price(10000)
                .stock(100)
                .build();
        testProduct = Product.createProduct(dto);
    }

    @Test
    @DisplayName("OrderProduct 생성 성공 - 재고 충분")
    void createOrderProductSuccess() {
        // given
        int quantity = 10;

        // when
        OrderProduct orderProduct = OrderProduct.createOrderProduct(testOrder, testProduct, quantity);

        // then
        assertThat(orderProduct).isNotNull();
        assertThat(orderProduct.getOrder()).isEqualTo(testOrder);
        assertThat(orderProduct.getProduct()).isEqualTo(testProduct);
        assertThat(orderProduct.getQuantity()).isEqualTo(10);
    }

    @Test
    @DisplayName("OrderProduct 생성 실패 - 재고 부족")
    void createOrderProductFailDueToInsufficientStock() {
        // given
        int quantity = 150; // 재고(100)보다 많은 수량

        // when & then
        assertThatThrownBy(() -> OrderProduct.createOrderProduct(testOrder, testProduct, quantity))
                .isInstanceOf(InsufficientStockException.class)
                .hasMessage("재고가 부족합니다.");
    }

    @Test
    @DisplayName("OrderProduct 생성 성공 - 재고와 수량이 같음")
    void createOrderProductSuccessWithExactStock() {
        // given
        int quantity = 100; // 재고와 동일한 수량

        // when
        OrderProduct orderProduct = OrderProduct.createOrderProduct(testOrder, testProduct, quantity);

        // then
        assertThat(orderProduct).isNotNull();
        assertThat(orderProduct.getQuantity()).isEqualTo(100);
    }
}