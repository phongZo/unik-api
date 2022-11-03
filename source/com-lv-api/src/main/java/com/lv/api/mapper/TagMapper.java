package com.lv.api.mapper;

import com.lv.api.dto.tag.TagDto;
import com.lv.api.form.tag.CreateTagForm;
import com.lv.api.form.tag.UpdateTagForm;
import com.lv.api.storage.model.Tag;
import org.mapstruct.*;

import java.util.List;

@Mapper(
        componentModel = "spring",
        unmappedTargetPolicy = ReportingPolicy.IGNORE,
        nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE
)
public interface TagMapper {

    @Named("fromCreateTagFromToEntityMapper")
    Tag fromCreateTagFromToEntity(CreateTagForm createTagForm);

    @Named("fromTagEntityToDtoMapper")
    TagDto fromTagEntityToDto(Tag tag);

    @Named("fromTagEntityToDtoAutoCompleteMapper")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(source = "id", target = "id")
    @Mapping(source = "tag", target = "tag")
    TagDto fromTagEntityToDtoAutoComplete(Tag tag);

    @Named("fromTagEntityListToDtoListMapper")
    @IterableMapping(elementTargetType = TagDto.class, qualifiedByName = "fromTagEntityToDtoMapper")
    List<TagDto> fromTagEntityListToDtoList(List<Tag> tags);

    @Named("fromTagEntityListToDtoListAutoCompleteMapper")
    @IterableMapping(elementTargetType = TagDto.class, qualifiedByName = "fromTagEntityToDtoAutoCompleteMapper")
    List<TagDto> fromTagEntityListToDtoListAutoComplete(List<Tag> tags);

    @Named("fromUpdateTagFromToEntityMapper")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(source = "tag", target = "tag")
    @Mapping(source = "color", target = "color")
    void fromUpdateTagFromToEntity(UpdateTagForm updateTagForm, @MappingTarget Tag tag);
}
