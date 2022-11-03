package com.lv.api.mapper;

import com.lv.api.dto.store.StoreDto;
import com.lv.api.form.store.CreateStoreForm;
import com.lv.api.form.store.UpdateStoreForm;
import com.lv.api.storage.model.Store;
import org.mapstruct.*;

import java.util.List;

@Mapper(
        componentModel = "spring",
        unmappedTargetPolicy = ReportingPolicy.IGNORE,
        nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE,
        uses = {LocationMapper.class}
)
public interface StoreMapper {

    @Named("fromCreateStoreFormToEntityMapper")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(source = "name", target = "name")
    @Mapping(source = "latitude", target = "latitude")
    @Mapping(source = "longitude", target = "longitude")
    @Mapping(source = "addressDetails", target = "addressDetails")
    Store fromCreateStoreFormToEntity(CreateStoreForm createStoreForm);

    @Named("fromStoreEntityToDto")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(source = "id", target = "id")
    @Mapping(source = "name", target = "name")
    @Mapping(source = "latitude", target = "latitude")
    @Mapping(source = "longitude", target = "longitude")
    @Mapping(source = "province", target = "province", qualifiedByName = "fromEntityToLocationDtoMapper")
    @Mapping(source = "district", target = "district", qualifiedByName = "fromEntityToLocationDtoMapper")
    @Mapping(source = "ward", target = "ward", qualifiedByName = "fromEntityToLocationDtoMapper")
    @Mapping(source = "addressDetails", target = "addressDetails")
    StoreDto fromStoreEntityToDto(Store store);

    @Named("fromStoreEntityListToDtoListMapper")
    @IterableMapping(elementTargetType = StoreDto.class, qualifiedByName = "fromStoreEntityToDto")
    List<StoreDto> fromStoreEntityListToDtoList(List<Store> storeList);

    @Named("fromUpdateStoreFormToEntityMapper")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(source = "name", target = "name")
    @Mapping(source = "latitude", target = "latitude")
    @Mapping(source = "longitude", target = "longitude")
    @Mapping(source = "addressDetails", target = "addressDetails")
    void fromUpdateStoreFormToEntity(UpdateStoreForm updateStoreForm, @MappingTarget Store store);
}
