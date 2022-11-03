package com.lv.api.dto.variant;

import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class VariantDto {

    @ApiModelProperty(name = "id")
    private Long id;

    @ApiModelProperty(name = "name")
    private String name;

    @ApiModelProperty(name = "price")
    private Double price;

    @ApiModelProperty(name = "description")
    private String description;
}
