package com.lv.api.form.variantconfig;

import com.lv.api.validation.VariantChoiceKind;
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
public class UpdateVariantConfigForm {

    @NotNull(message = "Id can not be null")
    @ApiModelProperty(name = "id", required = true)
    private Long id;

    @VariantChoiceKind
    @ApiModelProperty(name = "choiceKind", required = true)
    private Integer choiceKind;

    @NotNull(message = "Required can not be null")
    @ApiModelProperty(name = "isRequired", required = true)
    private Boolean isRequired;


    @NotBlank(message = "Name can not be blank")
    @ApiModelProperty(name = "name")
    private String name;

    @Size(min = 1, message = "List variant id can not be empty")
    @ApiModelProperty(name = "variants")
    private List<Long> variantIds = new ArrayList<>();
}
