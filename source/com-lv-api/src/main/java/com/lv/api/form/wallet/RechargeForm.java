package com.lv.api.form.wallet;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotNull;

@Data
public class RechargeForm {
    @NotNull(message = "money cannot be empty")
    @ApiModelProperty(required = true)
    private Double money;
}
