package com.lv.api.dto.group;

import com.lv.api.dto.ABasicAdminDto;
import com.lv.api.dto.permission.PermissionDto;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.List;

@Data
public class GroupAdminDto extends ABasicAdminDto {

    @ApiModelProperty(name = "name")
    private String name;
    @ApiModelProperty(name = "description")
    private String description;
    @ApiModelProperty(name = "kind")
    private int kind;
    @ApiModelProperty(name = "permissions")
    private List<PermissionDto> permissions;
}
