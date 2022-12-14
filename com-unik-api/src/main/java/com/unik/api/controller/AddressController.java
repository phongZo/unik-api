package com.unik.api.controller;

import com.unik.api.constant.Constants;
import com.unik.api.dto.ApiMessageDto;
import com.unik.api.dto.ErrorCode;
import com.unik.api.dto.ResponseListObj;
import com.unik.api.dto.customer.CustomerAddressDto;
import com.unik.api.exception.RequestException;
import com.unik.api.form.customer.CreateAddressForm;
import com.unik.api.form.customer.UpdateAddressForm;
import com.unik.api.mapper.CustomerAddressMapper;
import com.unik.api.storage.criteria.CustomerAddressCriteria;
import com.unik.api.storage.model.*;
import com.unik.api.storage.repository.CustomerAddressRepository;
import com.unik.api.storage.repository.CustomerRepository;
import com.unik.api.storage.repository.GroupRepository;
import com.unik.api.storage.model.Customer;
import com.unik.api.storage.model.CustomerAddress;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.transaction.Transactional;
import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/v1/addresses")
@CrossOrigin(origins = "*", allowedHeaders = "*")
@Slf4j
public class AddressController extends ABasicController{
    @Autowired
    CustomerAddressRepository addressRepository;

    @Autowired
    CustomerRepository customerRepository;

    @Autowired
    CustomerAddressMapper addressMapper;

    @Autowired
    GroupRepository groupRepository;

    @GetMapping(value = "/client-list",produces = MediaType.APPLICATION_JSON_VALUE)
    public ApiMessageDto<ResponseListObj<CustomerAddressDto>> clientList(CustomerAddressCriteria addressCriteria){
        if(!isCustomer()){
            throw new RequestException(ErrorCode.CUSTOMER_ADDRESS_ERROR_UNAUTHORIZED, "Not allowed list.");
        }
        ApiMessageDto<ResponseListObj<CustomerAddressDto>> responseListObjApiMessageDto = new ApiMessageDto<>();
        Customer customer = getCurrentCustomer();
        addressCriteria.setCustomerId(customer.getId());
        List<CustomerAddress> listAddress = addressRepository.findAll(addressCriteria.getSpecification());
        ResponseListObj<CustomerAddressDto> responseListObj = new ResponseListObj<>();
        responseListObj.setData(addressMapper.fromEntityListToAddressDto(listAddress));

        responseListObjApiMessageDto.setData(responseListObj);
        responseListObjApiMessageDto.setMessage("Get list success");
        return responseListObjApiMessageDto;
    }

    @GetMapping(value = "/client-get/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ApiMessageDto<CustomerAddressDto> clientGet(@PathVariable("id") Long id) {
        if(!isCustomer()){
            throw new RequestException(ErrorCode.CUSTOMER_ADDRESS_ERROR_UNAUTHORIZED, "Not allowed get.");
        }
        ApiMessageDto<CustomerAddressDto> result = new ApiMessageDto<>();
        CustomerAddress address = addressRepository.findById(id).orElse(null);
        if(address == null) {
            throw new RequestException(ErrorCode.CUSTOMER_ADDRESS_ERROR_NOT_FOUND, "Not found orders.");
        }
        CustomerAddressDto addressDto = addressMapper.fromEntityToDto(address);
        result.setData(addressDto);
        result.setMessage("Get address success");
        return result;
    }


    @PostMapping(value = "/client-create", produces = MediaType.APPLICATION_JSON_VALUE)
    @Transactional
    public ApiMessageDto<String> clientCreate(@Valid @RequestBody CreateAddressForm createAddressForm, BindingResult bindingResult) {
        if(!isCustomer()){
            throw new RequestException(ErrorCode.CUSTOMER_ADDRESS_ERROR_UNAUTHORIZED, "Not allowed to create.");
        }
        ApiMessageDto<String> apiMessageDto = new ApiMessageDto<>();
        // only 1 default address
        if(createAddressForm.getIsDefault()){
            CustomerAddress defaultAddress = addressRepository.findCustomerAddressByCustomerIdAndIsDefault(getCurrentCustomer().getId(),true);
            if(defaultAddress != null){
                defaultAddress.setIsDefault(false);
                addressRepository.save(defaultAddress);
            }
        }
        CustomerAddress address = addressMapper.fromCreateFormToEntity(createAddressForm);
        address.setCustomer(getCurrentCustomer());
        addressRepository.save(address);
        apiMessageDto.setMessage("Create address success");
        return apiMessageDto;
    }

    public Customer getCurrentCustomer(){
        Long id = getCurrentUserId();
        Customer customerCheck = customerRepository.findCustomerByAccountId(id);
        if (customerCheck == null || !customerCheck.getStatus().equals(Constants.STATUS_ACTIVE)) {
            throw new RequestException(ErrorCode.ORDERS_ERROR_NOT_FOUND, "Not found current customer");
        }
        return customerCheck;
    }

    @PutMapping(value = "/client-update", produces = MediaType.APPLICATION_JSON_VALUE)
    @Transactional
    public ApiMessageDto<String> clientUpdate(@Valid @RequestBody UpdateAddressForm updateAddressForm, BindingResult bindingResult) {
        if(!isCustomer()){
            throw new RequestException(ErrorCode.CUSTOMER_ADDRESS_ERROR_UNAUTHORIZED, "Not allowed to update.");
        }
        ApiMessageDto<String> apiMessageDto = new ApiMessageDto<>();
        CustomerAddress address = addressRepository.findById(updateAddressForm.getId()).orElse(null);
        if(address == null){
            throw new RequestException(ErrorCode.CUSTOMER_ADDRESS_ERROR_NOT_FOUND, "Not found address.");
        }

        // only 1 default address
        if(updateAddressForm.getIsDefault()){
            CustomerAddress defaultAddress = addressRepository.findCustomerAddressByCustomerIdAndIsDefault(getCurrentCustomer().getId(),true);
            if(defaultAddress != null){
                defaultAddress.setIsDefault(false);
                addressRepository.save(defaultAddress);
            }
        }
        addressMapper.fromUpdateFormToEntity(updateAddressForm, address);
        addressRepository.save(address);
        apiMessageDto.setMessage("Update address success");
        return apiMessageDto;
    }

}
