package com.aipia.tesk.controller;

import com.aipia.tesk.domain.Product;
import com.aipia.tesk.dto.ProductCreateDto;
import com.aipia.tesk.dto.ProductResponseDto;
import com.aipia.tesk.service.ProductService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/products")
@RequiredArgsConstructor
public class ProductController {

    private final ProductService productService;

    @PostMapping
    public ResponseEntity<ProductResponseDto> registerProduct(@Valid @RequestBody ProductCreateDto dto) {
        Product savedProduct = productService.registerProduct(dto);
        ProductResponseDto responseDto = ProductResponseDto.from(savedProduct);
        return ResponseEntity.status(HttpStatus.CREATED).body(responseDto);
    }

    @GetMapping("/{id}")
    public ResponseEntity<ProductResponseDto> getProductById(@PathVariable Long id) {
        Product product = productService.findProductById(id);
        ProductResponseDto responseDto = ProductResponseDto.from(product);
        return ResponseEntity.ok(responseDto);
    }

    @PatchMapping("/{id}/stock")
    public ResponseEntity<Void> updateStock(@PathVariable Long id, @RequestParam int quantity) {
        productService.updateStock(id, quantity);
        return ResponseEntity.ok().build();
    }
}
