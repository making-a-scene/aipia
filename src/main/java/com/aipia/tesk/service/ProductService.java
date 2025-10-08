package com.aipia.tesk.service;

import com.aipia.tesk.domain.Product;
import com.aipia.tesk.dto.ProductCreateDto;
import com.aipia.tesk.repository.ProductRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class ProductService {

    private final ProductRepository productRepository;

    @Transactional
    public Product registerProduct(ProductCreateDto dto) {
        Product product = Product.createProduct(dto);
        return productRepository.save(product);
    }
}
