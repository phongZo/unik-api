package com.lv.api.controller;

import com.lv.api.constant.Constants;
import com.lv.api.dto.ApiMessageDto;
import com.lv.api.dto.ErrorCode;
import com.lv.api.dto.ResponseListObj;
import com.lv.api.dto.customer.CustomerAdminDto;
import com.lv.api.dto.customer.CustomerDto;
import com.lv.api.dto.product.ProductAdminDto;
import com.lv.api.dto.product.ProductDto;
import com.lv.api.form.product.UpdateFavoriteForm;
import com.lv.api.form.wallet.RechargeForm;
import com.lv.api.exception.RequestException;
import com.lv.api.form.customer.*;
import com.lv.api.mapper.CustomerMapper;
import com.lv.api.mapper.ProductMapper;
import com.lv.api.service.CommonApiService;
import com.lv.api.storage.criteria.CustomerCriteria;
import com.lv.api.storage.criteria.ProductCriteria;
import com.lv.api.storage.model.Customer;
import com.lv.api.storage.model.Group;
import com.lv.api.storage.model.Product;
import com.lv.api.storage.repository.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.MediaType;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/v1/customer")
@CrossOrigin(origins = "*", allowedHeaders = "*")
@Slf4j
@RequiredArgsConstructor
public class CustomerController extends ABasicController {
    private final AccountRepository accountRepository;
    private final GroupRepository groupRepository;
    private final CustomerRepository customerRepository;
    private final CustomerAddressRepository customerAddressRepository;
    private final CustomerMapper customerMapper;
    private final CommonApiService commonApiService;

    @Autowired
    ProductMapper productMapper;

    @Autowired
    ProductRepository productRepository;

    @GetMapping(value = "/list", produces = MediaType.APPLICATION_JSON_VALUE)
    public ApiMessageDto<ResponseListObj<CustomerAdminDto>> list(CustomerCriteria customerCriteria, BindingResult bindingResult, Pageable pageable) {
        Page<Customer> customerPage = customerRepository.findAll(customerCriteria.getSpecification(), pageable);
        List<CustomerAdminDto> customerDtoList = customerMapper.fromListCustomerEntityToListAdminDto(customerPage.getContent());
        return new ApiMessageDto<>(new ResponseListObj<>(customerDtoList, customerPage), "Get list successfully");
    }


    private Customer getCurrentCustomer() {
        Customer customer = customerRepository.findCustomerByAccountId(getCurrentUserId());
        if(customer == null || !customer.getStatus().equals(Constants.STATUS_ACTIVE)){
            throw new RequestException(ErrorCode.CUSTOMER_ERROR_NOT_FOUND,"Customer not found");
        }
        return customer;
    }

    @GetMapping(value = "/auto-complete", produces = MediaType.APPLICATION_JSON_VALUE)
    public ApiMessageDto<List<CustomerDto>> autoComplete(CustomerCriteria customerCriteria) {
        Page<Customer> customerPage = customerRepository.findAll(customerCriteria.getSpecification(), Pageable.unpaged());
        return new ApiMessageDto<>(
                customerMapper.fromListCustomerEntityToListDto(customerPage.getContent()),
                "Auto complete customer successfully"
        );
    }

    @GetMapping(value = "/get/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ApiMessageDto<CustomerAdminDto> get(@PathVariable("id") Long id) {
        Customer customer = customerRepository.findById(id)
                .orElseThrow(() -> new RequestException(ErrorCode.CUSTOMER_ERROR_NOT_FOUND, "Customer not found"));
        return new ApiMessageDto<>(customerMapper.fromCustomerEntityToAdminDto(customer), "Get customer successfully");
    }

    @GetMapping(value = "/profile", produces = MediaType.APPLICATION_JSON_VALUE)
    public ApiMessageDto<CustomerDto> profile() {
        if(!isCustomer()){
            throw new RequestException(ErrorCode.CUSTOMER_ERROR_UNAUTHORIZED, "Not allowed get profile");
        }
        Customer customer = customerRepository.findCustomerByAccountId(getCurrentUserId());
        if (customer == null || !customer.getStatus().equals(Constants.STATUS_ACTIVE)){
            throw new RequestException(ErrorCode.CUSTOMER_ERROR_NOT_FOUND, "Customer not found");
        }
        return new ApiMessageDto<>(customerMapper.fromCustomerEntityToDto(customer), "Get customer successfully");
    }

    @PostMapping(value = "/create", produces = MediaType.APPLICATION_JSON_VALUE)
    public ApiMessageDto<String> create(@Valid @RequestBody CreateCustomerForm createCustomerForm, BindingResult bindingResult) {
        if (accountRepository.countAccountByUsernameOrEmailOrPhone(
                createCustomerForm.getUsername(), createCustomerForm.getEmail(), createCustomerForm.getPhone()
        ) > 0)
            throw new RequestException(ErrorCode.ACCOUNT_ERROR_EXISTED, "Account is existed");

        Group groupCustomer = groupRepository.findFirstByKind(Constants.GROUP_KIND_CUSTOMER);
        if (groupCustomer == null) {
            throw new RequestException(ErrorCode.GROUP_ERROR_NOT_FOUND);
        }
        Customer customer = customerMapper.fromCustomerCreateFormToEntity(createCustomerForm);
        customer.getAccount().setGroup(groupCustomer);
        customer.getAccount().setKind(Constants.USER_KIND_CUSTOMER);
        //rank?
        customerRepository.save(customer);
        return new ApiMessageDto<>("Create customer successfully");
    }

    @PostMapping(value = "/register", produces = MediaType.APPLICATION_JSON_VALUE)
    public ApiMessageDto<String> register(@Valid @RequestBody RegisterCustomerForm registerCustomerForm, BindingResult bindingResult) {
        if (accountRepository.countAccountByUsernameOrEmailOrPhone(
                registerCustomerForm.getUsername(), registerCustomerForm.getEmail(), registerCustomerForm.getPhone()
        ) > 0)
            throw new RequestException(ErrorCode.ACCOUNT_ERROR_EXISTED, "Account is existed");

        Group groupCustomer = groupRepository.findFirstByKind(Constants.GROUP_KIND_CUSTOMER);
        if (groupCustomer == null) {
            throw new RequestException(ErrorCode.GROUP_ERROR_NOT_FOUND);
        }
        Customer customer = customerMapper.fromCustomerRegisterFormToEntity(registerCustomerForm);
        customer.getAccount().setGroup(groupCustomer);
        customer.getAccount().setKind(Constants.USER_KIND_CUSTOMER);
        //rank?
        customerRepository.save(customer);
        return new ApiMessageDto<>("Create customer successfully");
    }

    @PostMapping(value = "/wallet-recharge", produces = MediaType.APPLICATION_JSON_VALUE)
    public ApiMessageDto<String> WalletRecharge(@Valid @RequestBody RechargeForm rechargeForm, BindingResult bindingResult) {
        if(!isCustomer()){
            throw new RequestException(ErrorCode.CUSTOMER_ERROR_UNAUTHORIZED, "Not allow recharge");
        }
        Customer customer = customerRepository.findCustomerByAccountId(getCurrentUserId());
        if(customer == null){
            throw new RequestException(ErrorCode.CUSTOMER_ERROR_NOT_FOUND, "Customer not found");
        }
        Double currentWalletMoney = customer.getWalletMoney();
        currentWalletMoney += rechargeForm.getMoney();
        customer.setWalletMoney(currentWalletMoney);
        customerRepository.save(customer);
        return new ApiMessageDto<>("Recharge successfully");
    }

    @PutMapping(value = "/update", produces = MediaType.APPLICATION_JSON_VALUE)
    public ApiMessageDto<String> update(@Valid @RequestBody UpdateCustomerForm updateCustomerForm, BindingResult bindingResult) {
        if (accountRepository.countAccountByPhoneOrEmail(
                updateCustomerForm.getPhone(), updateCustomerForm.getEmail()
        ) > 1)
            throw new RequestException(ErrorCode.ACCOUNT_ERROR_EXISTED, "Account is existed");
        Customer customer = customerRepository.findById(updateCustomerForm.getId())
                .orElseThrow(() -> new RequestException(ErrorCode.CUSTOMER_ERROR_NOT_FOUND, "Customer not found"));
        if (StringUtils.isNoneBlank(updateCustomerForm.getAvatar()) && !updateCustomerForm.getAvatar().equals(customer.getAccount().getAvatarPath()))
            commonApiService.deleteFile(customer.getAccount().getAvatarPath());
        customerMapper.fromUpdateCustomerFormToEntity(updateCustomerForm, customer);
        customerRepository.save(customer);
        accountRepository.save(customer.getAccount());
        return new ApiMessageDto<>("Update customer successfully");
    }

    @PutMapping(value = "/update-profile", produces = MediaType.APPLICATION_JSON_VALUE)
    public ApiMessageDto<String> updateProfile(@Valid @RequestBody UpdateProfileCustomerForm updateProfileCustomerForm, BindingResult bindingResult) {
        if (!isCustomer()) {
            throw new RequestException(ErrorCode.CUSTOMER_ERROR_UNAUTHORIZED, "Not allowed get list.");
        }
        Customer customer = customerRepository.findCustomerByAccountId(getCurrentUserId());
        if (customer == null || !customer.getStatus().equals(Constants.STATUS_ACTIVE)){
            throw new RequestException(ErrorCode.CUSTOMER_ERROR_NOT_FOUND, "Customer not found");
        }
        if (StringUtils.isNoneBlank(updateProfileCustomerForm.getAvatar()) && !updateProfileCustomerForm.getAvatar().equals(customer.getAccount().getAvatarPath()))
            commonApiService.deleteFile(customer.getAccount().getAvatarPath());
        customerMapper.fromUpdateProfileCustomerFormToEntity(updateProfileCustomerForm, customer);
        customerRepository.save(customer);
        accountRepository.save(customer.getAccount());
        return new ApiMessageDto<>("Update customer successfully");
    }

    @DeleteMapping(value = "/delete/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ApiMessageDto<String> delete(@PathVariable("id") Long id) {
        Customer customer = customerRepository.findById(id)
                .orElseThrow(() -> new RequestException(ErrorCode.CUSTOMER_ERROR_NOT_FOUND, "Customer not found"));
        commonApiService.deleteFile(customer.getAccount().getAvatarPath());
        customerRepository.delete(customer);
        return new ApiMessageDto<>("Delete customer successfully");
    }
}
