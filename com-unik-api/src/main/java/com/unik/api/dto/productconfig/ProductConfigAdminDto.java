package com.unik.api.dto.productconfig;

import com.unik.api.dto.ABasicAdminDto;
import com.unik.api.dto.productvariant.ProductVariantAdminDto;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class ProductConfigAdminDto extends ABasicAdminDto {

    @ApiModelProperty(name = "choiceKind")
    private Integer choiceKind;

    @ApiModelProperty(name = "isRequired")
    private Boolean isRequired;

    @ApiModelProperty(name = "name")
    private String name;

    @ApiModelProperty(name = "variants")
    private List<ProductVariantAdminDto> variants;
}
