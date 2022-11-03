package com.lv.api.dto.varianttemplate;

import com.lv.api.dto.variantconfig.VariantConfigDto;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class VariantTemplateDto {

    @ApiModelProperty(name = "id")
    private Long id;

    @ApiModelProperty(name = "name")
    private String name;

    @ApiModelProperty(name = "variantConfigs")
    private List<VariantConfigDto> variantConfigs;
}
