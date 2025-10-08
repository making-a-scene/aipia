package com.aipia.tesk.service;

import com.aipia.tesk.domain.Product;
import com.aipia.tesk.dto.ProductCreateDto;
import com.aipia.tesk.exception.InvalidProductException;
import com.aipia.tesk.repository.ProductRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
class ProductServiceTest {

    @Mock
    private ProductRepository productRepository;

    @InjectMocks
    private ProductService productService;

    private Product testProduct;

    @BeforeEach
    void setUp() {
        ProductCreateDto dto = ProductCreateDto.builder()
                .name("테스트 상품")
                .price(10000)
                .stock(100)
                .build();
        testProduct = Product.createProduct(dto);
    }

    @Test
    @DisplayName("ID로 상품 조회 성공 테스트")
    void findProductByIdSuccess() {
        // given
        given(productRepository.findById(1L)).willReturn(Optional.of(testProduct));

        // when
        Product foundProduct = productService.findProductById(1L);

        // then
        assertThat(foundProduct).isNotNull();
        assertThat(foundProduct.getName()).isEqualTo("테스트 상품");
        assertThat(foundProduct.getPrice()).isEqualTo(10000);
        assertThat(foundProduct.getStock()).isEqualTo(100);

        verify(productRepository).findById(1L);
    }

    @Test
    @DisplayName("존재하지 않는 ID로 상품 조회 시 예외 발생")
    void findProductByIdNotFound() {
        // given
        given(productRepository.findById(999L)).willReturn(Optional.empty());

        // when & then
        assertThatThrownBy(() -> productService.findProductById(999L))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("상품을 찾을 수 없습니다.");

        verify(productRepository).findById(999L);
    }

    @Test
    @DisplayName("상품 정상 등록 테스트")
    void createProductSuccess() {
        // given
        ProductCreateDto dto = ProductCreateDto.builder()
                .name("테스트 상품")
                .price(10000)
                .stock(100)
                .build();

        // when
        Product product = Product.createProduct(dto);

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
        ProductCreateDto dto = ProductCreateDto.builder()
                .name("이것은스물한글자를초과하는아주긴상품명입니다")
                .price(10000)
                .stock(100)
                .build();

        // when & then
        assertThatThrownBy(() -> Product.createProduct(dto))
                .isInstanceOf(InvalidProductException.class)
                .hasMessage("상품명은 20자를 초과할 수 없습니다.");
    }

    @Test
    @DisplayName("상품 등록 시 가격이 1억원을 초과하면 예외 발생")
    void createProductWithPriceExceeding100Million() {
        // given
        ProductCreateDto dto = ProductCreateDto.builder()
                .name("테스트 상품")
                .price(100_000_001)
                .stock(100)
                .build();

        // when & then
        assertThatThrownBy(() -> Product.createProduct(dto))
                .isInstanceOf(InvalidProductException.class)
                .hasMessage("상품 가격은 1억원을 초과할 수 없습니다.");
    }

    @Test
    @DisplayName("상품 재고 수정 시 재고가 음수가 되면 예외 발생")
    void updateStockResultingInNegative() {
        // given
        ProductCreateDto dto = ProductCreateDto.builder()
                .name("테스트 상품")
                .price(10000)
                .stock(10)
                .build();
        Product product = Product.createProduct(dto);

        // when & then
        assertThatThrownBy(() -> product.updateStock(-11))
                .isInstanceOf(InvalidProductException.class)
                .hasMessage("재고는 음수가 될 수 없습니다.");
    }
}
