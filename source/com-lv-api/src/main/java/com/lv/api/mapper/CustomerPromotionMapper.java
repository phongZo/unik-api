package com.lv.api.mapper;

import com.lv.api.dto.customer.CustomerPromotionDto;
import com.lv.api.form.customer.ApprovePromotionForm;
import com.lv.api.form.customer.RegisterCustomerForm;
import com.lv.api.storage.model.Customer;
import com.lv.api.storage.model.CustomerPromotion;
import org.mapstruct.*;

import java.util.List;
@Mapper(
        componentModel = "spring",
        unmappedTargetPolicy = ReportingPolicy.IGNORE,
        nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE
)
public interface CustomerPromotionMapper {
    @Named("fromApprovePromotionFormToEntity")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(source = "customerId", target = "customer.id")
    @Mapping(source = "expiryDate", target = "expireDate")
    CustomerPromotion fromApprovePromotionFormToEntity(ApprovePromotionForm approvePromotionForm);

    @Named("fromCustomerPromotionEntityToDtoMapper")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(source = "id", target = "id")
    @Mapping(source = "expireDate", target = "expireDate")
    @Mapping(source = "promotion.title", target = "title")
    @Mapping(source = "promotion.description", target = "description")
    @Mapping(source = "promotion.kind", target = "kind")
    @Mapping(source = "promotion.maxValueForPercent", target = "maxValueForPercent")
    @Mapping(source = "promotion.value", target = "value")
    CustomerPromotionDto fromCustomerPromotionEntityToDtoMapper(CustomerPromotion customerPromotion);

    @Named("fromListCustomerPromotionEntityToListDtoMapper")
    @IterableMapping(elementTargetType = CustomerPromotionDto.class, qualifiedByName = "fromCustomerPromotionEntityToDtoMapper")
    List<CustomerPromotionDto> fromListCustomerPromotionEntityToListDtoMapper(List<CustomerPromotion> customerPromotions);
}
