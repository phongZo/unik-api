package com.lv.api.mapper;

import com.lv.api.dto.product.ProductAdminDto;
import com.lv.api.dto.product.ProductDto;
import com.lv.api.form.product.CreateProductForm;
import com.lv.api.form.product.UpdateProductForm;
import com.lv.api.storage.model.Product;
import org.mapstruct.*;

import java.util.List;

@Mapper(
        componentModel = "spring",
        unmappedTargetPolicy = ReportingPolicy.IGNORE,
        nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE,
        uses = {ProductConfigMapper.class}
)
public interface ProductMapper {

    @Named("fromCreateProductFormToEntityMapper")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(source = "tags", target = "tags")
    @Mapping(source = "description", target = "description")
    @Mapping(source = "name", target = "name")
    @Mapping(source = "price", target = "price")
    @Mapping(source = "image", target = "image")
    @Mapping(source = "isSoldOut", target = "isSoldOut")
    @Mapping(source = "kind", target = "kind")
    @Mapping(source = "status", target = "status")
    @Mapping(source = "productConfigs", target = "productConfigs", qualifiedByName = "fromCreateProductConfigFormListToEntityListMapper")
    Product fromCreateProductFormToEntity(CreateProductForm createProductForm);

    @Named("fromProductEntityToDtoMapper")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(source = "id", target = "id")
    @Mapping(source = "category.id", target = "productCategoryId")
    @Mapping(source = "tags", target = "tags")
    @Mapping(source = "description", target = "description")
    @Mapping(source = "name", target = "name")
    @Mapping(source = "price", target = "price")
    @Mapping(source = "image", target = "image")
    @Mapping(source = "isSoldOut", target = "isSoldOut")
    @Mapping(source = "parentProduct.id", target = "parentProductId")
    @Mapping(source = "kind", target = "kind")
    ProductDto fromProductEntityToDto(Product product);

    @Named("fromProductEntityListToDtoListMapper")
    @IterableMapping(elementTargetType = ProductDto.class, qualifiedByName = "fromProductEntityToDtoMapper")
    List<ProductDto> fromProductEntityListToDtoList(List<Product> products);

    @Named("fromProductEntityToDtoAutoCompleteMapper")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(source = "id", target = "id")
    @Mapping(source = "name", target = "name")
    ProductDto fromProductEntityToDtoAutoComplete(Product product);

    @Named("fromProductEntityListToDtoListAutoCompleteMapper")
    @IterableMapping(elementTargetType = ProductDto.class, qualifiedByName = "fromProductEntityToDtoAutoCompleteMapper")
    List<ProductDto> fromProductEntityListToDtoListAutoComplete(List<Product> products);

    @Named("fromProductEntityToDtoDetails")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(source = "id", target = "id")
    @Mapping(source = "category.id", target = "productCategoryId")
    @Mapping(source = "tags", target = "tags")
    @Mapping(source = "description", target = "description")
    @Mapping(source = "name", target = "name")
    @Mapping(source = "price", target = "price")
    @Mapping(source = "image", target = "image")
    @Mapping(source = "isSoldOut", target = "isSoldOut")
    @Mapping(source = "parentProduct.id", target = "parentProductId")
    @Mapping(source = "kind", target = "kind")
    @Mapping(source = "productConfigs", target = "productConfigs", qualifiedByName = "fromProductConfigEntityListToDtoListMapper")
    ProductDto fromProductEntityToDtoDetails(Product product);

    @Named("fromProductEntityToAdminDtoMapper")
    @Mapping(source = "id", target = "id")
    @Mapping(source = "category.id", target = "productCategoryId")
    @Mapping(source = "tags", target = "tags")
    @Mapping(source = "description", target = "description")
    @Mapping(source = "name", target = "name")
    @Mapping(source = "price", target = "price")
    @Mapping(source = "image", target = "image")
    @Mapping(source = "isSoldOut", target = "isSoldOut")
    @Mapping(source = "parentProduct.id", target = "parentProductId")
    @Mapping(source = "kind", target = "kind")
    @Mapping(source = "productConfigs", target = "productConfigs", qualifiedByName = "fromProductConfigEntityListToAdminDtoListMapper")
    ProductAdminDto fromProductEntityToAdminDto(Product product);

    @Named("fromProductEntityListToAdminDtoListMapper")
    @IterableMapping(elementTargetType = ProductAdminDto.class, qualifiedByName = "fromProductEntityToAdminDtoMapper")
    List<ProductAdminDto> fromProductEntityListToAdminDtoList(List<Product> products);

    @Named("fromUpdateProductFormToEntityMapper")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(source = "tags", target = "tags")
    @Mapping(source = "description", target = "description")
    @Mapping(source = "name", target = "name")
    @Mapping(source = "price", target = "price")
    @Mapping(source = "image", target = "image")
    @Mapping(source = "isSoldOut", target = "isSoldOut")
    @Mapping(source = "status", target = "status")
    @Mapping(source = "productConfigs", target = "productConfigs", qualifiedByName = "frommUpdateProductConfigFormListToEntityListMapper")
    void fromUpdateProductFormToEntity(UpdateProductForm updateProductForm, @MappingTarget Product product);
}
