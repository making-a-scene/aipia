package com.aipia.tesk.service;

import com.aipia.tesk.model.Product;
import com.aipia.tesk.dto.ProductCreateDto;
import com.aipia.tesk.exception.ProductNotFoundException;
import com.aipia.tesk.repository.ProductRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class ProductService {

    private final ProductRepository productRepository;

    public Product findProductById(Long productId) {
        return productRepository.findById(productId)
                .orElseThrow(() -> new ProductNotFoundException("상품을 찾을 수 없습니다."));
    }

    @Transactional
    public Product registerProduct(ProductCreateDto dto) {
        Product product = Product.createProduct(dto);
        return productRepository.save(product);
    }

    @Transactional
    public void updateStock(Long productId, int quantity) {
        Product product = this.findProductById(productId);
        product.updateStock(quantity);
    }
}
