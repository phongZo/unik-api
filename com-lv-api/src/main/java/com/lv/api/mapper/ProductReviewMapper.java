package com.lv.api.mapper;

import com.lv.api.dto.review.ProductReviewDto;
import com.lv.api.form.review.ClientCreateProductReviewForm;
import com.lv.api.form.review.CreateProductReviewForm;
import com.lv.api.storage.model.ProductReview;
import org.mapstruct.*;

import java.util.List;

@Mapper(
        componentModel = "spring",
        unmappedTargetPolicy = ReportingPolicy.IGNORE,
        nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE,
        uses = {CustomerMapper.class}
)
public interface ProductReviewMapper {
    @Named("fromCreateFormToEntityMapper")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(source = "star", target = "star")
    @Mapping(source = "content", target = "content")
    ProductReview fromCreateFormToEntity(CreateProductReviewForm createProductReviewForm);

    @Named("fromClientCreateFormToEntityMapper")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(source = "productId", target = "product.id")
    @Mapping(source = "star", target = "star")
    @Mapping(source = "content", target = "content")
    ProductReview fromClientCreateFormToEntity(ClientCreateProductReviewForm createProductReviewForm);

    @Named("fromEntityToDtoMapper")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(source = "id", target = "id")
    @Mapping(source = "customer", target = "customerDto", qualifiedByName = "customerAutoCompleteMapping")
    @Mapping(source = "star", target = "star")
    @Mapping(source = "content", target = "content")
    @Mapping(source = "createdDate", target = "createdDate")
    ProductReviewDto fromEntityToDto(ProductReview productReview);

    @Named("fromListEntityToListDtoMapper")
    @IterableMapping(elementTargetType = ProductReviewDto.class, qualifiedByName = "fromEntityToDtoMapper")
    List<ProductReviewDto> fromListEntityToListDto(List<ProductReview> productReviewList);
}
