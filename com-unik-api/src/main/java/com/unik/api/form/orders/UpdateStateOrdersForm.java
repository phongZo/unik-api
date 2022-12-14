package com.unik.api.form.orders;

import com.unik.api.validation.OrdersState;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotNull;

@Data
public class UpdateStateOrdersForm {
    @NotNull(message = "id cannot be null")
    @ApiModelProperty(required = true)
    private Long id;

    @OrdersState
    @NotNull(message = "state cannot be null")
    @ApiModelProperty(required = true)
    private Integer state;
}
