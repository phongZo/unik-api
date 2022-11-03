package com.lv.api.dto.productvariant;

import com.lv.api.dto.ABasicAdminDto;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ProductVariantAdminDto extends ABasicAdminDto {

    @ApiModelProperty(name = "name")
    private String name;

    @ApiModelProperty(name = "price")
    private Double price;

    @ApiModelProperty(name = "description")
    private String description;

    @ApiModelProperty(name = "image")
    private String image;

    @ApiModelProperty(name = "orderSort")
    private String orderSort;

}
