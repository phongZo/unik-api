package com.lv.api.form.rank;

import com.lv.api.validation.RankTarget;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Getter
@Setter
public class CreateRankForm {

    @NotBlank(message = "Name can not be blank")
    @ApiModelProperty(required = true)
    private String name;

    @ApiModelProperty(required = true)
    private String avatar;

    @RankTarget
    @NotNull(message = "Target cannot be null")
    @ApiModelProperty(required = true)
    private Long target;
}
