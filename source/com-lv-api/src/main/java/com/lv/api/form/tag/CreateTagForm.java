package com.lv.api.form.tag;

import com.lv.api.validation.Hashtag;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CreateTagForm {

    @Hashtag
    @ApiModelProperty(name = "tag", required = true)
    private String tag;

    @ApiModelProperty(name = "color")
    private String color;
}
