package com.lv.api.mapper;

import com.lv.api.dto.orders.OrdersDetailDto;
import com.lv.api.form.orders.*;
import com.lv.api.storage.model.OrdersDetail;
import org.mapstruct.*;

import java.util.List;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE,
        nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE,
        uses = {OrdersMapper.class,ProductMapper.class})
public interface OrdersDetailMapper {

    @Mapping(source = "productId", target = "product.id")
    @Mapping(source = "amount", target = "amount")
    @Mapping(source = "note", target = "note")
    @BeanMapping(ignoreByDefault = true)
    @Named("adminCreateMapping")
    OrdersDetail fromCreateOrdersDetailFormToEntity(CreateOrdersDetailForm createOrdersDetailForm);

    @IterableMapping(elementTargetType = OrdersDetail.class, qualifiedByName = "adminCreateMapping")
    List<OrdersDetail> fromCreateOrdersDetailFormListToOrdersDetailList(List<CreateOrdersDetailForm> createOrdersDetailFormList);

    @Mapping(source = "id", target = "id")
    @Mapping(source = "orders", target = "ordersDto",qualifiedByName = "ordersAutoCompleteMapping")
    @Mapping(source = "product", target = "productDto")
    @Mapping(source = "price", target = "price")
    @Mapping(source = "amount", target = "amount")
    @BeanMapping(ignoreByDefault = true)
    @Named("adminGetMapping")
    OrdersDetailDto fromEntityToOrdersDetailDto(OrdersDetail ordersDetail);

    @IterableMapping(elementTargetType = OrdersDetailDto.class, qualifiedByName = "adminGetMapping")
    List<OrdersDetailDto> fromEntityListToOrdersDetailDtoList(List<OrdersDetail> ordersDetailList);

    @Mapping(source = "id", target = "id")
    @Mapping(source = "product", target = "productDto", qualifiedByName = "fromProductEntityToDtoMapper")
    @Mapping(source = "price", target = "price")
    @Mapping(source = "amount", target = "amount")
    @BeanMapping(ignoreByDefault = true)
    @Named("clientGetMapping")
    OrdersDetailDto fromEntityToOrdersDetailClientDto(OrdersDetail ordersDetail);

    @IterableMapping(elementTargetType = OrdersDetailDto.class, qualifiedByName = "clientGetMapping")
    List<OrdersDetailDto> fromEntityListToOrdersDetailClientDtoList(List<OrdersDetail> ordersDetailList);
}
