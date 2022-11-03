package com.lv.api.dto.store;

import com.lv.api.dto.location.LocationDto;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class StoreDto {

    @ApiModelProperty(name = "id")
    private Long id;

    @ApiModelProperty(name = "name")
    private String name;

    @ApiModelProperty(name = "latitude")
    private Double latitude;

    @ApiModelProperty(name = "longitude")
    private Double longitude;

    @ApiModelProperty(name = "province")
    private LocationDto province;

    @ApiModelProperty(name = "district")
    private LocationDto district;

    @ApiModelProperty(name = "ward")
    private LocationDto ward;

    @ApiModelProperty(name = "addressDetails")
    private String addressDetails;
}
