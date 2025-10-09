package com.aipia.tesk;

import com.aipia.tesk.model.Order;
import com.aipia.tesk.model.Product;
import com.aipia.tesk.dto.OrderProductDto;
import com.aipia.tesk.dto.ProductCreateDto;
import com.aipia.tesk.exception.ProductNotFoundException;
import com.aipia.tesk.repository.OrderProductRepository;
import com.aipia.tesk.repository.ProductRepository;
import com.aipia.tesk.service.OrderProductService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
class OrderProductServiceTest {

    @Mock
    private OrderProductRepository orderProductRepository;

    @Mock
    private ProductRepository productRepository;

    @InjectMocks
    private OrderProductService orderProductService;

    private Product testProduct1;
    private Product testProduct2;
    private Order testOrder;

    @BeforeEach
    void setUp() {
        ProductCreateDto dto1 = ProductCreateDto.builder()
                .name("상품1")
                .price(10000)
                .stock(100)
                .build();
        testProduct1 = Product.createProduct(dto1);

        ProductCreateDto dto2 = ProductCreateDto.builder()
                .name("상품2")
                .price(20000)
                .stock(50)
                .build();
        testProduct2 = Product.createProduct(dto2);

        testOrder = Order.builder()
                .id(1L)
                .build();
    }

    @Test
    @DisplayName("OrderProduct 엔티티 생성 성공")
    void createOrderProductsSuccess() {
        // given
        OrderProductDto orderProduct1 = OrderProductDto.builder()
                .productId(1L)
                .quantity(10)
                .build();

        OrderProductDto orderProduct2 = OrderProductDto.builder()
                .productId(2L)
                .quantity(5)
                .build();

        List<OrderProductDto> orderProducts = Arrays.asList(orderProduct1, orderProduct2);

        given(productRepository.findById(1L)).willReturn(Optional.of(testProduct1));
        given(productRepository.findById(2L)).willReturn(Optional.of(testProduct2));

        // when
        orderProductService.createOrderProducts(testOrder, orderProducts);

        // then
        verify(productRepository).findById(1L);
        verify(productRepository).findById(2L);
        verify(orderProductRepository).saveAll(any(List.class));
    }

    @Test
    @DisplayName("OrderProduct 생성 실패 - 존재하지 않는 상품 ID")
    void createOrderProductsFailDueToProductNotFound() {
        // given
        OrderProductDto orderProduct = OrderProductDto.builder()
                .productId(999L)
                .quantity(10)
                .build();

        List<OrderProductDto> orderProducts = Arrays.asList(orderProduct);

        given(productRepository.findById(999L)).willReturn(Optional.empty());

        // when & then
        assertThatThrownBy(() -> orderProductService.createOrderProducts(testOrder, orderProducts))
                .isInstanceOf(ProductNotFoundException.class)
                .hasMessage("상품을 찾을 수 없습니다.");

        verify(productRepository).findById(999L);
    }
}
