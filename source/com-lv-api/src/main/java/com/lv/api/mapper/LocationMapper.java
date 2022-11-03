package com.lv.api.mapper;

import com.lv.api.dto.location.LocationDto;
import com.lv.api.form.location.CreateLocationForm;
import com.lv.api.form.location.UpdateLocationForm;
import com.lv.api.storage.model.Location;
import org.mapstruct.*;

import java.util.List;

@Mapper(
        componentModel = "spring",
        unmappedTargetPolicy = ReportingPolicy.IGNORE,
        nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE
)
public interface LocationMapper {
    @Named("fromEntityToLocationDtoMapper")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(source = "id", target = "id")
    @Mapping(source = "name", target = "name")
    @Mapping(source = "parent.id", target = "parentId")
    @Mapping(source = "kind", target = "kind")
    LocationDto fromEntityToLocationDto(Location location);

    @Named("fromEntityListToLocationDtoListMapper")
    @IterableMapping(elementTargetType = LocationDto.class, qualifiedByName = "fromEntityToLocationDtoMapper")
    List<LocationDto> fromEntityListToLocationDtoList(List<Location> locations);

    @Named("fromCreateLocationFromToEntityMapper")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(source = "createLocationForm.name", target = "name")
    @Mapping(source = "parentLocation", target = "parent")
    @Mapping(source = "createLocationForm.kind", target = "kind")
    Location fromCreateLocationFromToEntity(CreateLocationForm createLocationForm, Location parentLocation);

    @Named("fromUpdateLocationFormToEntityMapper")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(source = "name", target = "name")
    @Mapping(source = "status", target = "status")
    void fromUpdateLocationFormToEntity(UpdateLocationForm updateLocationForm, @MappingTarget Location location);

    @Named("fromEntityToLocationDtoAutoCompleteMapper")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(source = "id", target = "id")
    @Mapping(source = "name", target = "name")
    @Mapping(source = "kind", target = "kind")
    LocationDto fromEntityToLocationDtoAutoComplete(Location location);

    @Named("fromEntityListToLocationDtoListAutoCompleteMapper")
    @IterableMapping(elementTargetType = LocationDto.class, qualifiedByName = "fromEntityToLocationDtoAutoCompleteMapper")
    List<LocationDto> fromEntityListToLocationDtoListAutoComplete(List<Location> locations);
}
