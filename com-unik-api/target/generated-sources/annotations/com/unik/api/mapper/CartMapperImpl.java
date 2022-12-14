package com.unik.api.mapper;

import com.unik.api.dto.cart.CartDto;
import com.unik.api.dto.cart.LineItemDto;
import com.unik.api.storage.model.Cart;
import com.unik.api.storage.model.LineItem;
import javax.annotation.processing.Generated;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2022-12-14T19:18:08+0700",
    comments = "version: 1.3.1.Final, compiler: javac, environment: Java 11.0.13 (Oracle Corporation)"
)
@Component
public class CartMapperImpl implements CartMapper {

    @Autowired
    private ProductVariantMapper productVariantMapper;

    @Override
    public CartDto fromEntityToCartDto(Cart cart) {
        if ( cart == null ) {
            return null;
        }

        CartDto cartDto = new CartDto();

        cartDto.setId( cart.getId() );
        cartDto.setStatus( cart.getStatus() );
        cartDto.setModifiedDate( cart.getModifiedDate() );
        cartDto.setCreatedDate( cart.getCreatedDate() );
        cartDto.setModifiedBy( cart.getModifiedBy() );
        cartDto.setCreatedBy( cart.getCreatedBy() );

        return cartDto;
    }

    @Override
    public LineItemDto fromEntityToLineItemDto(LineItem lineItem) {
        if ( lineItem == null ) {
            return null;
        }

        LineItemDto lineItemDto = new LineItemDto();

        lineItemDto.setProductVariantDto( productVariantMapper.fromProductVariantEntityToDto( lineItem.getVariant() ) );
        lineItemDto.setQuantity( lineItem.getQuantity() );
        lineItemDto.setId( lineItem.getId() );
        lineItemDto.setStatus( lineItem.getStatus() );
        lineItemDto.setModifiedDate( lineItem.getModifiedDate() );
        lineItemDto.setCreatedDate( lineItem.getCreatedDate() );
        lineItemDto.setModifiedBy( lineItem.getModifiedBy() );
        lineItemDto.setCreatedBy( lineItem.getCreatedBy() );

        return lineItemDto;
    }
}
