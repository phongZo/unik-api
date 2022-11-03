package com.lv.api.dto.group;

import com.lv.api.dto.permission.PermissionDto;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.List;

@Data
public class GroupDto {
    @ApiModelProperty(name = "id")
    private Long id;
    @ApiModelProperty(name = "name")
    private String name;
    @ApiModelProperty(name = "description")
    private String description;
    @ApiModelProperty(name = "kind")
    private int kind;
    @ApiModelProperty(name = "permissions")
    private List<PermissionDto> permissions ;
}
