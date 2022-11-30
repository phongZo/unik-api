package com.lv.api.mapper;

import com.lv.api.dto.category.CategoryDto;
import com.lv.api.dto.customer.CustomerAddressDto;
import com.lv.api.form.customer.CreateAddressForm;
import com.lv.api.form.customer.UpdateAddressForm;
import com.lv.api.storage.model.Category;
import com.lv.api.storage.model.CustomerAddress;
import org.mapstruct.*;

import java.util.List;

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

    @IterableMapping(elementTargetType = CustomerAddressDto.class, qualifiedByName = "clientGetCustomerAddress")
    List<CustomerAddressDto> fromEntityListToAddressDto(List<CustomerAddress> addressList);

    @Mapping(source = "addressDetails", target = "addressDetails")
    @Mapping(source = "receiverFullName", target = "receiverFullName")
    @Mapping(source = "phone", target = "phone")
    @Mapping(source = "isDefault", target = "isDefault")
    @Mapping(source = "typeAddress", target = "typeAddress")
    @BeanMapping(ignoreByDefault = true)
    @Named("createCustomerAddress")
    CustomerAddress fromCreateFormToEntity(CreateAddressForm address);

    @Mapping(source = "id", target = "id")
    @Mapping(source = "addressDetails", target = "addressDetails")
    @Mapping(source = "receiverFullName", target = "receiverFullName")
    @Mapping(source = "phone", target = "phone")
    @Mapping(source = "isDefault", target = "isDefault")
    @Mapping(source = "typeAddress", target = "typeAddress")
    @BeanMapping(ignoreByDefault = true)
    @Named("updateCustomerAddress")
    CustomerAddress fromUpdateFormToEntity(UpdateAddressForm address);
}
