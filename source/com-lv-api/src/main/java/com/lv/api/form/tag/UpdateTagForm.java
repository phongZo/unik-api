package com.lv.api.form.tag;

import com.lv.api.validation.Hashtag;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Getter
@Setter
public class UpdateTagForm {

    @NotNull(message = "Id can not be null")
    @ApiModelProperty(name = "id")
    private Long id;

    @Hashtag
    @ApiModelProperty(name = "tag", required = true)
    private String tag;

    @ApiModelProperty(name = "color")
    private String color;
}
