package com.aipia.tesk.service;

import com.aipia.tesk.model.Order;
import com.aipia.tesk.model.OrderProduct;
import com.aipia.tesk.model.Product;
import com.aipia.tesk.dto.OrderProductDto;
import com.aipia.tesk.exception.ProductNotFoundException;
import com.aipia.tesk.repository.OrderProductRepository;
import com.aipia.tesk.repository.ProductRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class OrderProductService {

    private final OrderProductRepository orderProductRepository;
    private final ProductRepository productRepository;

    @Transactional
    public void createOrderProducts(Order order, List<OrderProductDto> orderProducts) {
        List<OrderProduct> orderProductList = new ArrayList<>();

        for (OrderProductDto orderProductDto : orderProducts) {
            Product product = productRepository.findById(orderProductDto.getProductId())
                    .orElseThrow(() -> new ProductNotFoundException("상품을 찾을 수 없습니다."));

            // 검증과 동시에 엔티티 생성 (재고 부족 시 예외 발생)
            OrderProduct orderProduct = OrderProduct.createOrderProduct(order, product, orderProductDto.getQuantity());
            orderProductList.add(orderProduct);
        }

        orderProductRepository.saveAll(orderProductList);
    }
}