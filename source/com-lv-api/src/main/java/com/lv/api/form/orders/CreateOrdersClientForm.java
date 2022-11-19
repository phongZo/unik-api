package com.lv.api.form.orders;

import com.lv.api.validation.PaymentMethod;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.util.List;

@Data
public class CreateOrdersClientForm {
    @NotNull(message = "customerAddressId cannot be empty")
    @ApiModelProperty(required = true)
    private Long customerAddressId;

    @PaymentMethod
    @NotNull(message = "paymentMethod cannot be null")
    @ApiModelProperty(required = true)
    private Integer paymentMethod;

    @NotNull(message = "storeId cannot be empty")
    @ApiModelProperty(required = true)
    private Long storeId;

    private Long promotionId;
    private Double saleOff = 0d;

    @NotEmpty(message = "createOrdersDetailFormList cannot be empty")
    @ApiModelProperty(required = true)
    private List<CreateOrdersDetailForm> createOrdersDetailFormList;

}
