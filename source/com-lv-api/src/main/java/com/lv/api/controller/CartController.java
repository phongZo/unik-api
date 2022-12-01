package com.lv.api.controller;

import com.lv.api.constant.Constants;
import com.lv.api.dto.ApiMessageDto;
import com.lv.api.dto.ErrorCode;
import com.lv.api.dto.cart.CartDto;
import com.lv.api.dto.cart.LineItemDto;
import com.lv.api.exception.RequestException;
import com.lv.api.form.cart.AddItemForm;
import com.lv.api.form.cart.UpdateCartQuantity;
import com.lv.api.mapper.CartMapper;
import com.lv.api.storage.model.*;
import com.lv.api.storage.repository.CartRepository;
import com.lv.api.storage.repository.CustomerRepository;
import com.lv.api.storage.repository.LineItemRepository;
import com.lv.api.storage.repository.ProductRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.sound.sampled.Line;
import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/v1/cart")
@CrossOrigin(origins = "*", allowedHeaders = "*")
@Slf4j
public class CartController extends ABasicController{
    @Autowired
    CartRepository cartRepository;

    @Autowired
    LineItemRepository lineItemRepository;

    @Autowired
    CustomerRepository customerRepository;

    @Autowired
    CartMapper cartMapper;

    @Autowired
    ProductRepository productRepository;

    @GetMapping(value = "/client-cart",produces = MediaType.APPLICATION_JSON_VALUE)
    public ApiMessageDto<CartDto> getClientCart(){
        if(!isCustomer()){
            throw new RequestException(ErrorCode.CART_ERROR_UNAUTHORIZED,"Not allow get .");
        }
        Customer customer = getCurrentCustomer();
        ApiMessageDto<CartDto> result = new ApiMessageDto<>();
        Cart cart = cartRepository.findByCustomerId(customer.getId());

        // if cart null will create one
        if(cart == null){
            cart = new Cart();
            cart.setCustomer(customer);
            cart = cartRepository.save(cart);
        }
        CartDto dto = cartMapper.fromEntityToCartDto(cart);
        List<LineItem> list = lineItemRepository.findByCartId(cart.getId());
        if(list != null && !list.isEmpty()){
            Double totalMoney = 0d;
            for (LineItem lineItem : list){
                Product product = lineItem.getProduct();
                LineItemDto lineItemDto = cartMapper.fromEntityToLineItemDto(lineItem);
                dto.getLineItemDtoList().add(lineItemDto);
                if(product.getIsSaleOff()){
                    float saleOffValue = 0;
                    if(product.getSaleOff() != null){
                        saleOffValue = (float)product.getSaleOff() / 100;
                    }
                    totalMoney += product.getPrice() * (1 - saleOffValue);
                }
                totalMoney += product.getPrice();
            }
            dto.setTotalMoney(totalMoney);
        }
        result.setData(dto);
        result.setMessage("Get cart success");
        return result;
    }

    @PostMapping(value = "/add-item", produces = MediaType.APPLICATION_JSON_VALUE)
    public ApiMessageDto<String> addItem(@Valid @RequestBody AddItemForm addItemForm, BindingResult bindingResult) {
        if(!isCustomer()){
            throw new RequestException(ErrorCode.CART_ERROR_UNAUTHORIZED, "Not allowed to add item.");
        }
        ApiMessageDto<String> apiMessageDto = new ApiMessageDto<>();
        Product product = productRepository.findById(addItemForm.getProductId()).orElse(null);
        if(product == null || product.getIsSoldOut()|| !product.getStatus().equals(Constants.STATUS_ACTIVE)){
            throw new RequestException(ErrorCode.PRODUCT_NOT_FOUND, "Not found product.");
        }
        Cart cart = cartRepository.findByCustomerId(getCurrentCustomer().getId());
        if(cart == null){
            cart = new Cart();
            cart.setCustomer(getCurrentCustomer());
            cart = cartRepository.save(cart);
        }
        LineItem lineItem = lineItemRepository.findByCartIdAndProductId(cart.getId(),product.getId());
        if(lineItem != null) lineItem.setQuantity(lineItem.getQuantity() + 1);
        else {
            lineItem = new LineItem();
            lineItem.setCart(cart);
            lineItem.setProduct(product);
            lineItem.setQuantity(1);
        }
        lineItemRepository.save(lineItem);

        apiMessageDto.setMessage("Add cart success");
        return apiMessageDto;
    }

    @PutMapping(value = "/items/quantity", produces = MediaType.APPLICATION_JSON_VALUE)
    public ApiMessageDto<String> updateQuantity(@Valid @RequestBody UpdateCartQuantity updateCartQuantity, BindingResult bindingResult) {
        if(!isCustomer()){
            throw new RequestException(ErrorCode.PRODUCT_REVIEW_ERROR_UNAUTHORIZED, "Not allowed to update quantity.");
        }
        ApiMessageDto<String> apiMessageDto = new ApiMessageDto<>();
        LineItem lineItem = lineItemRepository.findById(updateCartQuantity.getLineItemId()).orElse(null);
        if(lineItem == null){
            throw new RequestException(ErrorCode.LINE_ITEM_ERROR_NOT_FOUND, "Not found item.");
        }
        lineItem.setQuantity(updateCartQuantity.getQuantity());
        lineItemRepository.save(lineItem);
        apiMessageDto.setMessage("Update quantity success");
        return apiMessageDto;
    }

    private Customer getCurrentCustomer() {
        Customer customer = customerRepository.findCustomerByAccountId(getCurrentUserId());
        if(customer == null || !customer.getStatus().equals(Constants.STATUS_ACTIVE)){
            throw new RequestException(ErrorCode.CUSTOMER_ERROR_NOT_FOUND,"Customer not found");
        }
        return customer;
    }

    @DeleteMapping(value = "/items/delete/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ApiMessageDto<String> delete(@PathVariable(name = "id") Long id) {
        LineItem lineItem = lineItemRepository.findById(id)
                .orElseThrow(() -> new RequestException(ErrorCode.LINE_ITEM_ERROR_NOT_FOUND, "Item not found"));

        lineItemRepository.delete(lineItem);
        return new ApiMessageDto<>("Delete item successfully");
    }
}
