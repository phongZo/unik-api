package com.lv.api.dto.product;

import com.lv.api.dto.ABasicAdminDto;
import com.lv.api.dto.productconfig.ProductConfigAdminDto;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class ProductAdminDto extends ABasicAdminDto {

    @ApiModelProperty(name = "productCategoryId")
    private Long productCategoryId;

    @ApiModelProperty(name = "tags")
    private String tags;

    @ApiModelProperty(name = "description")
    private String description;

    @ApiModelProperty(name = "name")
    private String name;

    @ApiModelProperty(name = "price")
    private Double price;

    @ApiModelProperty(name = "image")
    private String image;

    @ApiModelProperty(name = "isSoldOut")
    private Boolean isSoldOut;

    @ApiModelProperty(name = "parentProductId")
    private Long parentProductId;

    @ApiModelProperty(name = "kind")
    private Integer kind;

    @ApiModelProperty(name = "productConfigs")
    private List<ProductConfigAdminDto> productConfigs;
}
