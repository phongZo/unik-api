package com.lv.api.form.varianttemplate;

import com.lv.api.form.variantconfig.UpdateVariantConfigForm;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
public class UpdateVariantTemplateForm {

    @NotNull(message = "Message can not be null")
    @ApiModelProperty(name = "id", required = true)
    private Long id;

    @NotBlank(message = "Name can not be null")
    @ApiModelProperty(name = "name", required = true)
    private String name;

    @Size(min = 1, message = "List variant config can not be empty")
    @ApiModelProperty(name = "variantConfigs")
    private List<UpdateVariantConfigForm> variantConfigs = new ArrayList<>();
}
