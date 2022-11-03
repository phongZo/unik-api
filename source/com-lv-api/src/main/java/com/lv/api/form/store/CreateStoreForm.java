package com.lv.api.form.store;

import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Getter
@Setter
public class CreateStoreForm {

    @NotBlank(message = "Name can not be blank")
    @ApiModelProperty(name = "name", required = true)
    private String name;

    @ApiModelProperty(name = "latitude")
    private Double latitude;

    @ApiModelProperty(name = "longitude")
    private Double longitude;

    @NotNull(message = "Province id can not be null")
    @ApiModelProperty(name = "provinceId", required = true)
    private Long provinceId;

    @NotNull(message = "District id can not be null")
    @ApiModelProperty(name = "districtId", required = true)
    private Long districtId;

    @NotNull(message = "Ward id can not be null")
    @ApiModelProperty(name = "wardId", required = true)
    private Long wardId;

    @NotBlank(message = "Address details can not be blank")
    @ApiModelProperty(name = "addressDetails", required = true)
    private String addressDetails;
}
