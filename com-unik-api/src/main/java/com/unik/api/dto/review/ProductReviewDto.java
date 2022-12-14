package com.unik.api.dto.review;

import com.unik.api.dto.ABasicAdminDto;
import com.unik.api.dto.customer.CustomerDto;
import lombok.Data;

@Data
public class ProductReviewDto extends ABasicAdminDto {
    private CustomerDto customerDto;
    private Integer star;
    private String content;
}
