package com.lv.api.mapper;

import com.lv.api.dto.variantconfig.VariantConfigDto;
import com.lv.api.form.variantconfig.CreateVariantConfigForm;
import com.lv.api.form.variantconfig.UpdateVariantConfigForm;
import com.lv.api.storage.model.VariantConfig;
import org.mapstruct.*;

import java.util.List;

@Mapper(
        componentModel = "spring",
        unmappedTargetPolicy = ReportingPolicy.IGNORE,
        nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE,
        uses = {VariantMapper.class}
)
public interface VariantConfigMapper {
    @Named("fromCreateVariantConfigFormToEntityMapper")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(source = "choiceKind", target = "choiceKind")
    @Mapping(source = "isRequired", target = "isRequired")
    @Mapping(source = "name", target = "name")
    VariantConfig fromCreateVariantConfigFormToEntity(CreateVariantConfigForm createVariantConfigForm);

    @Named("fromVariantConfigEntityToDtoMapper")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(source = "id", target = "id")
    @Mapping(source = "choiceKind", target = "choiceKind")
    @Mapping(source = "isRequired", target = "isRequired")
    @Mapping(source = "name", target = "name")
    @Mapping(source = "variants", target = "variants", qualifiedByName = "fromVariantEntityListToDtoListMapper")
    VariantConfigDto fromVariantConfigEntityToDto(VariantConfig variantConfig);

    @Named("fromVariantConfigEntityListToDtoListMapper")
    @IterableMapping(elementTargetType = VariantConfigDto.class, qualifiedByName = "fromVariantConfigEntityToDtoMapper")
    List<VariantConfigDto> fromVariantConfigEntityListToDtoList(List<VariantConfig> variantConfigs);

    @Named("formUpdateVariantConfigFormToEntityMapper")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(source = "choiceKind", target = "choiceKind")
    @Mapping(source = "isRequired", target = "isRequired")
    @Mapping(source = "name", target = "name")
    void formUpdateVariantConfigFormToEntity(UpdateVariantConfigForm updateVariantTemplateForm, @MappingTarget VariantConfig variantConfig);
}
