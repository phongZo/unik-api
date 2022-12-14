package com.unik.api.mapper;

import com.unik.api.dto.promotion.PromotionDto;
import com.unik.api.form.promotion.CreatePromotionForm;
import com.unik.api.storage.model.Promotion;
import org.mapstruct.*;

@Mapper(
        componentModel = "spring",
        unmappedTargetPolicy = ReportingPolicy.IGNORE,
        nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE
)
public interface PromotionMapper {
    @Named("fromCreateFormToEntityMapper")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(source = "title", target = "title")
    @Mapping(source = "description", target = "description")
    @Mapping(source = "kind", target = "kind")
    @Mapping(source = "maxValueForPercent", target = "maxValueForPercent")
    @Mapping(source = "value", target = "value")
    Promotion fromCreateFormToEntity(CreatePromotionForm createPromotionForm);

    @Named("fromEntityToDtoMapper")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(source = "id", target = "id")
    @Mapping(source = "title", target = "title")
    @Mapping(source = "description", target = "description")
    @Mapping(source = "kind", target = "kind")
    @Mapping(source = "maxValueForPercent", target = "maxValueForPercent")
    @Mapping(source = "value", target = "value")
    @Mapping(source = "status", target = "status")
    PromotionDto fromEntityToRankDto(Promotion promotion);
}
