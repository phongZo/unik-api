package com.lv.api.mapper;

import com.lv.api.dto.permission.PermissionAdminDto;
import com.lv.api.dto.permission.PermissionDto;
import com.lv.api.form.permission.UpdatePermissionForm;
import com.lv.api.storage.model.Permission;
import org.mapstruct.*;

import java.util.List;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface PermissionMapper {

    @Mapping(source = "name", target = "name")
    @Mapping(source = "action", target = "action")
    @Mapping(source = "showMenu", target = "showMenu")
    @Mapping(source = "description", target = "description")
    @Mapping(source = "nameGroup", target = "nameGroup")
    @Mapping(source = "status", target = "status")
    PermissionDto fromEntityToDto(Permission permission);

    @Mapping(source = "name", target = "name")
    @Mapping(source = "action", target = "action")
    @Mapping(source = "showMenu", target = "showMenu")
    @Mapping(source = "description", target = "description")
    @Mapping(source = "nameGroup", target = "nameGroup")
    PermissionAdminDto fromEntityToAdminDto(Permission permission);

    @IterableMapping(elementTargetType = PermissionAdminDto.class)
    List<PermissionAdminDto> fromEntityListToAdminDtoList(List<Permission> content);

    @Named("fromUpdatePermissionFormToEntityMapper")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(source = "name", target = "name")
    @Mapping(source = "action", target = "action")
    @Mapping(source = "showMenu", target = "showMenu")
    @Mapping(source = "description", target = "description")
    @Mapping(source = "nameGroup", target = "nameGroup")
    void fromUpdatePermissionFormToEntity(UpdatePermissionForm updatePermissionForm, @MappingTarget Permission permission);
}
