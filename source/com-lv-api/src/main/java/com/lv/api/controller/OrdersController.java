package com.lv.api.controller;

import com.lv.api.constant.Constants;
import com.lv.api.dto.ApiMessageDto;
import com.lv.api.dto.ErrorCode;
import com.lv.api.dto.ResponseListObj;
import com.lv.api.dto.orders.*;
import com.lv.api.exception.RequestException;
import com.lv.api.form.orders.CreateOrdersClientForm;
import com.lv.api.form.orders.UpdateStateOrdersForm;
import com.lv.api.mapper.OrdersDetailMapper;
import com.lv.api.mapper.OrdersMapper;
import com.lv.api.service.CommonApiService;
import com.lv.api.storage.criteria.OrdersCriteria;
import com.lv.api.storage.model.*;
import com.lv.api.storage.repository.*;
import com.lv.api.utils.StringUtils;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.time.DateUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.MediaType;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.sound.sampled.Line;
import javax.transaction.Transactional;
import javax.validation.Valid;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@RestController
@RequestMapping("/v1/orders")
@CrossOrigin(origins = "*", allowedHeaders = "*")
@Slf4j
public class OrdersController extends ABasicController{
    @Autowired
    OrdersRepository ordersRepository;

    @Autowired
    OrdersDetailRepository ordersDetailRepository;

    @Autowired
    CustomerRepository customerRepository;

    @Autowired
    LineItemRepository lineItemRepository;

    @Autowired
    EmployeeRepository employeeRepository;

    @Autowired
    AccountRepository accountRepository;

    @Autowired
    CartRepository cartRepository;

    @Autowired
    ProductRepository productRepository;

    @Autowired
    CustomerAddressRepository customerAddressRepository;

    @Autowired
    CustomerPromotionRepository customerPromotionRepository;

    @Autowired
    StoreRepository storeRepository;

    @Autowired
    OrdersMapper ordersMapper;

    @Autowired
    OrdersDetailMapper ordersDetailMapper;

    @Autowired
    CommonApiService landingIsApiService;


    @GetMapping(value = "/list",produces = MediaType.APPLICATION_JSON_VALUE)
    public ApiMessageDto<ResponseListObj<OrdersDto>> list(OrdersCriteria ordersCriteria, Pageable pageable){
        if(!isAdmin()){
            throw new RequestException(ErrorCode.ORDERS_ERROR_UNAUTHORIZED,"Not allowed get list.");
        }
        ApiMessageDto<ResponseListObj<OrdersDto>> responseListObjApiMessageDto = new ApiMessageDto<>();
        Page<Orders> listOrders = ordersRepository.findAll(ordersCriteria.getSpecification(), pageable);
        ResponseListObj<OrdersDto> responseListObj = new ResponseListObj<>();
        responseListObj.setData(ordersMapper.fromEntityListToOrdersDtoList(listOrders.getContent()));
        responseListObj.setPage(pageable.getPageNumber());
        responseListObj.setTotalPage(listOrders.getTotalPages());
        responseListObj.setTotalElements(listOrders.getTotalElements());

        responseListObjApiMessageDto.setData(responseListObj);
        responseListObjApiMessageDto.setMessage("Get list success");
        return responseListObjApiMessageDto;
    }

    // Store
    @GetMapping(value = "/my-orders",produces = MediaType.APPLICATION_JSON_VALUE)
    public ApiMessageDto<ResponseListObjOrders> listMyOrders(OrdersCriteria ordersCriteria, Pageable pageable){
        Long posId = getCurrentPosId();
        Store store = storeRepository.findById(posId).orElse(null);
        if(store == null || !store.getStatus().equals(Constants.STATUS_ACTIVE)){
            throw new RequestException(ErrorCode.STORE_ERROR_NOT_FOUND,"store not found");
        }
        ApiMessageDto<ResponseListObjOrders> responseListObjApiMessageDto = new ApiMessageDto<>();
        Page<Orders> listOrders = ordersRepository.findAll(ordersCriteria.getSpecification(), pageable);
        ResponseListObjOrders responseListObjOrders = new ResponseListObjOrders();
        responseListObjOrders.setData(ordersMapper.fromEntityListToOrdersDtoList(listOrders.getContent()));
        responseListObjOrders.setPage(pageable.getPageNumber());
        responseListObjOrders.setTotalPage(listOrders.getTotalPages());
        responseListObjOrders.setTotalElements(listOrders.getTotalElements());
        Double sum = ordersRepository.sumMoney(listOrders.getContent());
        responseListObjOrders.setSumMoney(sum);
        responseListObjApiMessageDto.setData(responseListObjOrders);
        responseListObjApiMessageDto.setMessage("Get list success");
        return responseListObjApiMessageDto;
    }

    @GetMapping(value = "/get/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ApiMessageDto<OrdersDto> get(@PathVariable("id") Long id) {
        if(!isAdmin()){
            throw new RequestException(ErrorCode.ORDERS_ERROR_UNAUTHORIZED, "Not allowed get.");
        }
        ApiMessageDto<OrdersDto> result = new ApiMessageDto<>();

        Orders orders = ordersRepository.findById(id).orElse(null);
        if(orders == null) {
            throw new RequestException(ErrorCode.ORDERS_ERROR_NOT_FOUND, "Not found orders.");
        }
        List<OrdersDetailDto> ordersDetailDtoList = ordersDetailMapper
                .fromEntityListToOrdersDetailDtoList(ordersDetailRepository.findAllByOrdersId(id));
        OrdersDto ordersDto = ordersMapper.fromEntityToOrdersDto(orders);
        ordersDto.setOrdersDetailDtoList(ordersDetailDtoList);
        result.setData(ordersDto);
        result.setMessage("Get orders success");
        return result;
    }


    @GetMapping(value = "/client-list",produces = MediaType.APPLICATION_JSON_VALUE)
    public ApiMessageDto<ResponseListObj<OrdersDto>> clientList(OrdersCriteria ordersCriteria){
        if(!isCustomer()){
            throw new RequestException(ErrorCode.ORDERS_ERROR_UNAUTHORIZED, "Not allowed get.");
        }
        ApiMessageDto<ResponseListObj<OrdersDto>> responseListObjApiMessageDto = new ApiMessageDto<>();
        Customer customer = getCurrentCustomer();
        ordersCriteria.setCustomerId(customer.getId());
        List<Orders> listOrders = ordersRepository.findAll(ordersCriteria.getSpecification());
        List<OrdersDetail> listFirstDetail = ordersDetailRepository.findFirstByListOrders(listOrders);
        List<OrdersDto> dto = ordersMapper.fromEntityListToOrdersDtoList(listOrders);
        for (OrdersDto ordersDto : dto){
            List<OrdersDetail> detailList = new ArrayList<>();
            for (OrdersDetail detail : listFirstDetail){
                if(detail.getOrders().getId().equals(ordersDto.getId())){
                    detailList.add(detail);
                }
            }
            ordersDto.setOrdersDetailDtoList(ordersDetailMapper.fromEntityListToOrdersDetailDtoList(detailList));
        }
        ResponseListObj<OrdersDto> responseListObj = new ResponseListObj<>();
        responseListObj.setData(dto);
        responseListObjApiMessageDto.setData(responseListObj);
        responseListObjApiMessageDto.setMessage("Get list success");
        return responseListObjApiMessageDto;
    }

    @GetMapping(value = "/client-get/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ApiMessageDto<OrdersDto> clientGet(@PathVariable("id") Long id) {
        if(!isCustomer()){
            throw new RequestException(ErrorCode.ORDERS_ERROR_UNAUTHORIZED, "Not allowed get.");
        }
        ApiMessageDto<OrdersDto> result = new ApiMessageDto<>();
        Customer customer = getCurrentCustomer();
        Orders orders = ordersRepository.findOrdersByIdAndCustomerId(id,customer.getId());
        if(orders == null || !orders.getStatus().equals(Constants.STATUS_ACTIVE)) {
            throw new RequestException(ErrorCode.ORDERS_ERROR_NOT_FOUND, "Not found orders.");
        }
        List<OrdersDetailDto> ordersDetailDtoList = ordersDetailMapper.fromEntityListToOrdersDetailClientDtoList(ordersDetailRepository.findAllByOrdersId(id));
        OrdersDto ordersDto = ordersMapper.fromEntityToOrdersDto(orders);
        ordersDto.setOrdersDetailDtoList(ordersDetailDtoList);
        result.setData(ordersDto);
        result.setMessage("Get orders success");
        return result;
    }

    private Customer getCurrentCustomer() {
        Long userId = getCurrentUserId();
        Customer customer = customerRepository.findCustomerByAccountId(userId);
        if(customer == null || !customer.getStatus().equals(Constants.STATUS_ACTIVE)){
            throw new RequestException(ErrorCode.CUSTOMER_ERROR_NOT_FOUND, "Not found current customer.");
        }
        return customer;
    }

    @PostMapping(value = "/client-create", produces = MediaType.APPLICATION_JSON_VALUE)
    @Transactional
    public ApiMessageDto<String> clientCreate(@Valid @RequestBody CreateOrdersClientForm createOrdersForm, BindingResult bindingResult) {
        if(!isCustomer()){
            throw new RequestException(ErrorCode.ORDERS_ERROR_UNAUTHORIZED, "Not allowed to create.");
        }
        ApiMessageDto<String> apiMessageDto = new ApiMessageDto<>();
        Store store = checkStore(createOrdersForm);
        CustomerAddress address = checkAddress(createOrdersForm);
        CustomerPromotion promotion = null;
        if(createOrdersForm.getPromotionId() != null){
            promotion = checkPromotion(createOrdersForm);
        }

        List<OrdersDetail> ordersDetailList = ordersDetailMapper
                .fromCreateOrdersDetailFormListToOrdersDetailList(createOrdersForm.getCreateOrdersDetailFormList());
        Orders orders = ordersMapper.fromCreateOrdersFormToEntity(createOrdersForm);
        orders.setStore(store);
        orders.setAddress(address);
        setCustomerClient(orders,createOrdersForm);
        Double checkSaleOff = createOrdersForm.getSaleOff();
        if(checkSaleOff < 0){
            throw new RequestException(ErrorCode.ORDERS_ERROR_BAD_REQUEST, "saleOff is not accepted");
        }
        orders.setCode(generateCode());
        orders.setState(Constants.ORDERS_STATE_CREATED);

        // calculate amount item
        Integer amount = 0;
        for (OrdersDetail detail : ordersDetailList){
            amount += detail.getAmount();
        }
        orders.setAmount(amount);
        Orders savedOrder = ordersRepository.save(orders);
        /*-----------------------Xử lý orders detail------------------ */
        amountPriceCal(orders,ordersDetailList,savedOrder,promotion);  //Tổng tiền hóa đơn
        ordersDetailRepository.saveAll(ordersDetailList);

        /*-----------------------Quay lại xử lý orders------------------ */
        if(promotion != null){
            orders.setCustomerPromotionId(promotion.getId());
            promotion.setIsInUse(true);
            customerPromotionRepository.save(promotion);
        }
        ordersRepository.save(orders);

        // remove cart item
        Cart cart = cartRepository.findByCustomerId(getCurrentCustomer().getId());
        List<LineItem> list = lineItemRepository.findByCartId(cart.getId());
        lineItemRepository.deleteAll(list);
        cartRepository.save(cart);
        apiMessageDto.setMessage("Create orders success");
        return apiMessageDto;
    }

    private Store checkStore(CreateOrdersClientForm createOrdersForm) {
        Store store = storeRepository.findById(createOrdersForm.getStoreId()).orElse(null);
        if(store == null || !store.getStatus().equals(Constants.STATUS_ACTIVE)){
            throw new RequestException(ErrorCode.STORE_ERROR_NOT_FOUND, "Not found store");
        }
        return store;
    }

    private CustomerPromotion checkPromotion(CreateOrdersClientForm createOrdersForm) {
        CustomerPromotion promotion = customerPromotionRepository.findById(createOrdersForm.getPromotionId()).orElse(null);
        if(promotion == null || promotion.getExpireDate().before(new Date())){
            throw new RequestException(ErrorCode.CUSTOMER_PROMOTION_ERROR_NOT_FOUND, "Not found promotion");
        }
        if(promotion.getIsInUse()){
            throw new RequestException(ErrorCode.CUSTOMER_PROMOTION_ERROR_IN_ANOTHER_USING, "Promotion is in another other using");
        }
        return promotion;
    }

    private CustomerAddress checkAddress(CreateOrdersClientForm createOrdersForm) {
        CustomerAddress address = customerAddressRepository.findById(createOrdersForm.getCustomerAddressId()).orElse(null);
        if(address == null) {
            throw new RequestException(ErrorCode.CUSTOMER_ADDRESS_ERROR_NOT_FOUND, "Not found address");
        }
        return address;
    }

    private void setCustomerClient(Orders orders, CreateOrdersClientForm createOrdersForm) {
        Long id = getCurrentUserId();
        Customer customerCheck = customerRepository.findCustomerByAccountId(id);
        if (customerCheck == null || !customerCheck.getStatus().equals(Constants.STATUS_ACTIVE)) {
            throw new RequestException(ErrorCode.ORDERS_ERROR_NOT_FOUND, "Not found current customer");
        }
        orders.setCustomer(customerCheck);
    }

    @PutMapping(value = "/client-cancel-orders/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    @Transactional
    public ApiMessageDto<String> clientCancelOrders(@PathVariable("id") Long id) {
        if (!isCustomer()) {
            throw new RequestException(ErrorCode.ORDERS_ERROR_UNAUTHORIZED, "Not allowed to cancel orders.");
        }
        ApiMessageDto<String> apiMessageDto = new ApiMessageDto<>();
        Orders orders = ordersRepository.findById(id).orElse(null);
        if(orders == null){
            throw new RequestException(ErrorCode.ORDERS_ERROR_NOT_FOUND, "Order Not Found");
        }
        checkState(orders);
        checkCancelTime(orders);
        Integer prevState = orders.getState();
        orders.setState(Constants.ORDERS_STATE_CANCELED);
        orders.setPrevState(prevState);
        ordersRepository.save(orders);
        apiMessageDto.setMessage("Cancel order success");
        return apiMessageDto;
    }

    private void checkCancelTime(Orders orders) {
        Integer limitCancelTime =  Constants.LIMIT_CANCEL_ORDER_TIME;

        Date oldDate = orders.getCreatedDate();
        Date checkDate = DateUtils.addHours(oldDate, limitCancelTime);  //Cộng 3 giờ vào ngày tạo orders
        Date currentDate = new Date();
        if (!checkDate.after(currentDate)){
                throw new RequestException(ErrorCode.ORDERS_ERROR_BAD_REQUEST, "Order time was over the limit cancel time");
        }
    }

    // STORE
    @PutMapping(value = "/update-state", produces = MediaType.APPLICATION_JSON_VALUE)
    @Transactional
    public ApiMessageDto<String> update(@Valid @RequestBody UpdateStateOrdersForm updateStateOrdersForm, BindingResult bindingResult) {
        ApiMessageDto<String> apiMessageDto = new ApiMessageDto<>();
        Orders orders = ordersRepository.findById(updateStateOrdersForm.getId()).orElse(null);
        if(orders == null){
            throw new RequestException(ErrorCode.ORDERS_ERROR_NOT_FOUND, "Orders Not Found");
        }
        checkNewState(updateStateOrdersForm,orders);
        Integer prevState = orders.getState();
        orders.setState(updateStateOrdersForm.getState());
        orders.setPrevState(prevState);

        // UPDATE SOLD AMOUNT OF PRODUCT
        if(orders.getState().equals(Constants.ORDERS_STATE_COMPLETED)){
            List<OrdersDetail> ordersDetailList = ordersDetailRepository.findAllByOrdersId(orders.getId());
            for (OrdersDetail ordersDetail : ordersDetailList){
                Product productCheck = productRepository.findById(ordersDetail.getProduct().getId()).orElse(null);
                if (productCheck == null){
                    throw new RequestException(ErrorCode.PRODUCT_NOT_FOUND, "product not existed");
                }
                Integer soldAmount = productCheck.getSoldAmount() + 1;
                productCheck.setSoldAmount(soldAmount);
                productRepository.save(productCheck);
            }
        }
        ordersRepository.save(orders);
        apiMessageDto.setMessage("Update orders state success");
        return apiMessageDto;
    }


    private void amountPriceCal(Orders orders,List<OrdersDetail> ordersDetailList, Orders savedOrder, CustomerPromotion promotion) {
        int checkIndex = 0;
        double amountPrice = 0.0;
        for (OrdersDetail ordersDetail : ordersDetailList){
            Product productCheck = productRepository.findById(ordersDetail.getProduct().getId()).orElse(null);
            if (productCheck == null){
                throw new RequestException(ErrorCode.PRODUCT_NOT_FOUND, "product in index "+checkIndex+"is not existed");
            }
            Double productPrice = productCheck.getPrice();
            if(productCheck.getIsSaleOff()){
                productPrice = productPrice - (productPrice * productCheck.getSaleOff() / 100);
            }
            ordersDetail.setPrice(productPrice);
            amountPrice = amountPrice + productPrice * (ordersDetail.getAmount()); // Tổng tiền 1 sp
            ordersDetail.setOrders(savedOrder);
            checkIndex++;
        }
        Double totalMoney = totalMoneyHaveToPay(amountPrice,orders,promotion);
        totalMoney += Constants.DEFAULT_DELIVERY_FEE;
        orders.setTotalMoney(totalMoney);
        orders.setVat(Constants.ORDER_VAT);
    }

    private Double totalMoneyHaveToPay(Double amountPrice,Orders orders, CustomerPromotion promotion) {
        if(promotion == null){
            double VAT = (double)Constants.ORDER_VAT / 100;             // Tính VAT (%)
            amountPrice = amountPrice + amountPrice * VAT ;              // Tiền sau cùng bằng tiền sau khi saleOff cộng với VAT (10% tổng tiền)
            return Math.round(amountPrice * 100.0) / 100.0;
        }
        Integer kind = promotion.getPromotion().getKind();
        double saleOff = 0d;
        double amountAfterSaleOff = 0d;
        if(kind.equals(Constants.PROMOTION_KIND_MONEY)){
            saleOff = Double.parseDouble(promotion.getPromotion().getValue());
            amountAfterSaleOff -= saleOff;
        } else if (kind.equals(Constants.PROMOTION_KIND_PERCENT)){
            int percent = Integer.parseInt(promotion.getPromotion().getValue());
            amountAfterSaleOff -= amountAfterSaleOff * ((double)percent / 100);
        }
        double VAT = (double)Constants.ORDER_VAT / 100;             // Tính VAT (%)
        amountPrice = amountAfterSaleOff + amountAfterSaleOff * VAT ;              // Tiền sau cùng bằng tiền sau khi saleOff cộng với VAT (10% tổng tiền)
        return Math.round(amountPrice * 100.0) / 100.0;          // Làm tròn đến thập phân thứ 2
    }

    private void checkNewState(UpdateStateOrdersForm updateStateOrdersForm,Orders orders) {
        // state mới phải lớn hơn state cũ
        if((updateStateOrdersForm.getState() <= orders.getState())){
            throw new RequestException(ErrorCode.ORDERS_ERROR_BAD_REQUEST, "Update orders state must mor than or equal old state");
        }
        // State 3 4 không thể update
        Integer state = orders.getState();
        if(state.equals(Constants.ORDERS_STATE_COMPLETED) || state.equals(Constants.ORDERS_STATE_CANCELED)){
            throw new RequestException(ErrorCode.ORDERS_ERROR_BAD_REQUEST, "Can not update orders in state 3 or 4");
        }
    }

    private String generateCode() {
        String code = StringUtils.generateRandomString(8);
        code = code.replace("0", "A");
        code = code.replace("O", "Z");
        return code;
    }

    private void checkState(Orders orders) {
        Integer state = orders.getState();
        if(state.equals(Constants.ORDERS_STATE_COMPLETED) || state.equals(Constants.ORDERS_STATE_CANCELED)){
            throw new RequestException(ErrorCode.ORDERS_ERROR_BAD_REQUEST, "Can not cancel order in this state");
        }
    }
}
