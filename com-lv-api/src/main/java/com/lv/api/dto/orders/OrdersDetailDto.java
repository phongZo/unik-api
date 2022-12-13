package com.lv.api.dto.orders;

import com.lv.api.dto.ABasicAdminDto;
import com.lv.api.dto.product.ProductDto;
import com.lv.api.dto.productvariant.ProductVariantDto;
import lombok.Data;

@Data
public class OrdersDetailDto extends ABasicAdminDto {
    private OrdersDto ordersDto;
    private ProductDto productDto;
    private ProductVariantDto productVariantDto;
    private Double price;
    private Integer amount;
    private String note;
    private Boolean isReviewed;
}
