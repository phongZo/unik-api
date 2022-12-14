package com.unik.api.dto.cart;

import com.unik.api.dto.ABasicAdminDto;
import com.unik.api.dto.product.ProductDto;
import com.unik.api.dto.productvariant.ProductVariantDto;
import lombok.Data;

@Data
public class LineItemDto extends ABasicAdminDto {
    private ProductDto productDto;
    private ProductVariantDto productVariantDto;
    private Integer quantity;
}
