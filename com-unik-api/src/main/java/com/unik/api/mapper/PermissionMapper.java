package com.unik.api.mapper;

import com.unik.api.dto.permission.PermissionAdminDto;
import com.unik.api.dto.permission.PermissionDto;
import com.unik.api.form.permission.UpdatePermissionForm;
import com.unik.api.storage.model.Permission;
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
