package com.lv.api.form.productconfig;

import com.lv.api.form.productvariant.UpdateProductVariantForm;
import com.lv.api.validation.Status;
import com.lv.api.validation.VariantChoiceKind;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
public class UpdateProductConfigForm {

    @ApiModelProperty(name = "id")
    private Long id;

    @VariantChoiceKind
    @ApiModelProperty(name = "choiceKind", required = true)
    private Integer choiceKind;

    @NotNull
    @ApiModelProperty(name = "isRequired", required = true)
    private Boolean isRequired;

    @NotBlank
    @ApiModelProperty(name = "name", required = true)
    private String name;

    @Status
    @ApiModelProperty(name = "status")
    private Integer status = 1;

    @NotEmpty(message = "Product variant can not be empty")
    @ApiModelProperty(name = "variants", required = true)
    List<@Valid UpdateProductVariantForm> variants = new ArrayList<>();
}
