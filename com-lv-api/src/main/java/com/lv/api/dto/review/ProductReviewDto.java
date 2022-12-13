package com.lv.api.dto.review;

import com.lv.api.dto.ABasicAdminDto;
import com.lv.api.dto.customer.CustomerDto;
import lombok.Data;

@Data
public class ProductReviewDto extends ABasicAdminDto {
    private CustomerDto customerDto;
    private Integer star;
    private String content;
}
