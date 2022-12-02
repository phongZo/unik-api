package com.lv.api.dto.orders;

import com.lv.api.dto.ABasicAdminDto;
import com.lv.api.dto.product.ProductDto;
import lombok.Data;

@Data
public class OrdersDetailDto extends ABasicAdminDto {
    private OrdersDto ordersDto;
    private ProductDto productDto;
    private Double price;
    private Integer amount;
    private String note;
    private Boolean isReviewed;
}
