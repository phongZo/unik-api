package com.lv.api.dto.cart;

import com.lv.api.dto.ABasicAdminDto;
import com.lv.api.dto.product.ProductDto;
import com.lv.api.dto.productvariant.ProductVariantDto;
import com.lv.api.storage.model.ProductVariant;
import lombok.Data;

@Data
public class LineItemDto extends ABasicAdminDto {
    private ProductDto productDto;
    private ProductVariantDto productVariantDto;
    private Integer quantity;
}
