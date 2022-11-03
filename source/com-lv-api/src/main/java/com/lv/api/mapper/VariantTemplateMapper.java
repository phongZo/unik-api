package com.lv.api.mapper;

import com.lv.api.dto.varianttemplate.VariantTemplateDto;
import com.lv.api.form.varianttemplate.CreateVariantTemplateForm;
import com.lv.api.form.varianttemplate.UpdateVariantTemplateForm;
import com.lv.api.storage.model.VariantTemplate;
import org.mapstruct.*;

import java.util.List;

@Mapper(
        componentModel = "spring",
        unmappedTargetPolicy = ReportingPolicy.IGNORE,
        nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE
)
public interface VariantTemplateMapper {

    @Named("fromCreateVariantTemplateFormToEntityMapper")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(source = "name", target = "name")
    VariantTemplate fromCreateVariantTemplateFormToEntity(CreateVariantTemplateForm createVariantTemplateForm);

    @Named("fromVariantTemplateEntityToDtoMapper")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(source = "id", target = "id")
    @Mapping(source = "name", target = "name")
    VariantTemplateDto fromVariantTemplateEntityToDto(VariantTemplate variantTemplate);

    @Named("fromVariantTemplateEntityListToDtoListMapper")
    @IterableMapping(elementTargetType = VariantTemplateDto.class, qualifiedByName = "fromVariantTemplateEntityToDtoMapper")
    List<VariantTemplateDto> fromVariantTemplateEntityListToDtoList(List<VariantTemplate> variantTemplates);

    @Named("fromUpdateVariantTemplateFormToEntityMapper")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(source = "name", target = "name")
    void fromUpdateVariantTemplateFormToEntity(UpdateVariantTemplateForm updateVariantTemplateForm, @MappingTarget VariantTemplate variantTemplate);
}
