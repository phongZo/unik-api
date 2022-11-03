package com.lv.api.form.customer;

import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Getter
@Setter
public class UpdateAddressForm {
    @NotNull(message = "Id can not be null")
    @ApiModelProperty(name = "id", required = true)
    private Long id;

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

    @NotBlank(message = "Receiver full name can not be blank")
    @ApiModelProperty(name = "receiverFullName", required = true)
    private String receiverFullName;

    @NotBlank(message = "Phone number can not be blank")
    @ApiModelProperty(name = "phone", required = true)
    private String phone;

    @NotNull(message = "Default address can not be null")
    @ApiModelProperty(name = "isDefault", required = true)
    private Boolean isDefault;

    @ApiModelProperty(name = "note")
    private String note;
}
