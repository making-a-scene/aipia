package com.aipia.tesk.dto;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ProductCreateDto {

    @NotBlank(message = "상품명은 필수입니다.")
    @Size(max = 20, message = "상품명은 20자를 초과할 수 없습니다.")
    private String name;

    @Positive(message = "상품 가격은 양수여야 합니다.")
    @Max(value = 100_000_000, message = "상품 가격은 1억원을 초과할 수 없습니다.")
    private int price;

    @Positive(message = "재고는 양수여야 합니다.")
    private int stock;
}
