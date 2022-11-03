package com.lv.api.form.location;

import com.lv.api.validation.LocationKind;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Getter
@Setter
public class CreateLocationForm {
    @NotBlank(message = "Location name can not blank")
    @ApiModelProperty(required = true)
    private String name;
    private Long parentId;
    @NotNull(message = "Kind cannot be null")
    @ApiModelProperty(required = true)
    @LocationKind
    private Integer kind;
}
