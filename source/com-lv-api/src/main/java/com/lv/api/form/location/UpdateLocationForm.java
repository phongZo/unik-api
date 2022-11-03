package com.lv.api.form.location;

import com.lv.api.validation.LocationKind;
import com.lv.api.validation.Status;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Getter
@Setter
public class UpdateLocationForm {
    @NotNull(message = "Id cannot be null")
    @ApiModelProperty(required = true)
    private Long id;
    @NotBlank(message = "Location name can not blank")
    @ApiModelProperty(required = true)
    private String name;
    @Status
    @NotNull(message = "Status cannot be null")
    @ApiModelProperty(required = true)
    private Integer status;
}
