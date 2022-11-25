package com.lv.api.dto.cart;

import com.lv.api.dto.ABasicAdminDto;
import com.lv.api.dto.product.ProductDto;
import lombok.Data;

@Data
public class LineItemDto extends ABasicAdminDto {
    private ProductDto productDto;
    private Integer quantity;
}
