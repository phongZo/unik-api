package com.lv.api.mapper;

import com.lv.api.dto.customer.CustomerAddressDto;
import com.lv.api.storage.model.CustomerAddress;
import org.mapstruct.*;

@Mapper(
        componentModel = "spring",
        unmappedTargetPolicy = ReportingPolicy.IGNORE,
        nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE,
        uses = {CustomerMapper.class}
)
public interface CustomerAddressMapper {
    @Mapping(source = "id", target = "id")
    @Mapping(source = "customer", target = "customerDto", qualifiedByName = "fromCustomerEntityToDtoMapper")
    @Mapping(source = "addressDetails", target = "addressDetails")
    @Mapping(source = "receiverFullName", target = "receiverFullName")
    @Mapping(source = "phone", target = "phone")
    @Mapping(source = "isDefault", target = "isDefault")
    @Mapping(source = "note", target = "note")
    @Mapping(source = "status", target = "status")
    @Mapping(source = "modifiedDate", target = "modifiedDate")
    @Mapping(source = "createdDate", target = "createdDate")
    @Mapping(source = "modifiedBy", target = "modifiedBy")
    @Mapping(source = "createdBy", target = "createdBy")
    @BeanMapping(ignoreByDefault = true)
    @Named("clientGetCustomerAddress")
    CustomerAddressDto fromEntityToDto(CustomerAddress address);
}
