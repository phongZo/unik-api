package com.lv.api.controller;

import com.lv.api.constant.Constants;
import com.lv.api.dto.ApiMessageDto;
import com.lv.api.dto.ErrorCode;
import com.lv.api.dto.ResponseListObj;
import com.lv.api.dto.review.ProductReviewDto;
import com.lv.api.exception.RequestException;
import com.lv.api.form.review.ClientCreateProductReviewForm;
import com.lv.api.form.review.CreateProductReviewForm;
import com.lv.api.mapper.ProductReviewMapper;
import com.lv.api.storage.criteria.ProductReviewCriteria;
import com.lv.api.storage.model.*;
import com.lv.api.storage.repository.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.MediaType;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
@RequestMapping("/v1/product-review")
@CrossOrigin(origins = "*", allowedHeaders = "*")
@Slf4j
public class ProductReviewController extends ABasicController{
    @Autowired
    ProductReviewMapper productReviewMapper;

    @Autowired
    ProductReviewRepository productReviewRepository;

    @Autowired
    ProductRepository productRepository;

    @Autowired
    CustomerRepository customerRepository;

    @Autowired
    OrdersRepository ordersRepository;

    @Autowired
    OrdersDetailRepository ordersDetailRepository;

    // all can see product review
    @GetMapping(value = "/list",produces = MediaType.APPLICATION_JSON_VALUE) //
    public ApiMessageDto<ResponseListObj<ProductReviewDto>> list(ProductReviewCriteria productReviewCriteria, Pageable pageable){
        ApiMessageDto<ResponseListObj<ProductReviewDto>> responseListObjApiMessageDto = new ApiMessageDto<>();
        Page<ProductReview> list = productReviewRepository.findAll(productReviewCriteria.getSpecification(), pageable);

        ResponseListObj<ProductReviewDto> responseListObj = new ResponseListObj<>();
        responseListObj.setData(productReviewMapper.fromListEntityToListDto(list.getContent()));

        responseListObj.setPage(pageable.getPageNumber());
        responseListObj.setTotalPage(list.getTotalPages());
        responseListObj.setTotalElements(list.getTotalElements());

        responseListObjApiMessageDto.setData(responseListObj);
        responseListObjApiMessageDto.setMessage("Get list success");
        return responseListObjApiMessageDto;
    }

    @PostMapping(value = "/create", produces = MediaType.APPLICATION_JSON_VALUE)
    public ApiMessageDto<String> create(@Valid @RequestBody CreateProductReviewForm createProductReviewForm, BindingResult bindingResult) {
        if(!isAdmin()){
            throw new RequestException(ErrorCode.PRODUCT_REVIEW_ERROR_UNAUTHORIZED, "Not allowed to create.");
        }
        ApiMessageDto<String> apiMessageDto = new ApiMessageDto<>();
        Product product = checkProduct(createProductReviewForm.getProductId());
        Customer customer = checkCustomer(createProductReviewForm.getCustomerId());
        ProductReview reviewCheck = productReviewRepository.findByProductIdAndCustomerId(product.getId(),customer.getId());
        if(reviewCheck != null){
            throw new RequestException(ErrorCode.PRODUCT_REVIEW_ERROR_BAD_REQUEST, "Customer already reviewed this product.");
        }
        ProductReview productReview = productReviewMapper.fromCreateFormToEntity(createProductReviewForm);
        productReview.setProduct(product);
        productReview.setCustomer(customer);
        productReviewRepository.save(productReview);

        Integer productTotalReview = product.getTotalReview() + 1;
        float productNewAvgStar = (product.getAvgStar() * product.getTotalReview() + 1) / productTotalReview;

        product.setAvgStar((float) Math.round(productNewAvgStar * 10) / 10);
        product.setTotalReview(productTotalReview);
        productRepository.save(product);
        apiMessageDto.setMessage("Create Product Review success");
        return apiMessageDto;
    }

    @PostMapping(value = "/client-create", produces = MediaType.APPLICATION_JSON_VALUE)
    public ApiMessageDto<String> clientCreate(@Valid @RequestBody ClientCreateProductReviewForm createProductReviewForm, BindingResult bindingResult) {
        if(!isCustomer()){
            throw new RequestException(ErrorCode.PRODUCT_REVIEW_ERROR_UNAUTHORIZED, "Not allowed to create.");
        }
        ApiMessageDto<String> apiMessageDto = new ApiMessageDto<>();
        Product product = checkProduct(createProductReviewForm.getProductId());
        Customer customer = getCurrentCustomer();
        checkOrder(createProductReviewForm.getOrdersDetailId(),product);

        ProductReview productReview = productReviewMapper.fromClientCreateFormToEntity(createProductReviewForm);
        productReview.setProduct(product);
        productReview.setCustomer(customer);
        productReviewRepository.save(productReview);
        Integer productTotalReview = product.getTotalReview() + 1;
        float productNewAvgStar = (product.getAvgStar() * product.getTotalReview() + 1) / productTotalReview;

        product.setAvgStar((float) Math.round(productNewAvgStar * 10) / 10);
        product.setTotalReview(productTotalReview);
        productRepository.save(product);
        apiMessageDto.setMessage("Create Product Review success");
        return apiMessageDto;
    }

    private Customer getCurrentCustomer() {
        Customer customer = customerRepository.findCustomerByAccountId(getCurrentUserId());
        if (customer == null || !customer.getStatus().equals(Constants.STATUS_ACTIVE)){
            throw new RequestException(ErrorCode.CUSTOMER_ERROR_NOT_FOUND, "Customer not found.");
        }
        return customer;
    }

    private void checkOrder(Long ordersDetailId, Product product) {
        OrdersDetail detail = ordersDetailRepository.findById(ordersDetailId).orElse(null);
        if(detail == null || !detail.getOrders().getCustomer().getAccount().getId().equals(getCurrentUserId())){
            throw new RequestException(ErrorCode.ORDERS_DETAIL_ERROR_NOT_FOUND, "Orders detail not found.");
        }
        if(!detail.getProduct().getId().equals(product.getId())){
            throw new RequestException(ErrorCode.ORDERS_DETAIL_ERROR_BAD_REQUEST, "Product not belongs to detail.");
        }
        detail.setIsReviewed(true);
        ordersDetailRepository.save(detail);
    }

    private Customer checkCustomer(Long customerId) {
        Customer customer = customerRepository.findById(customerId).orElse(null);
        if (customer == null || !customer.getStatus().equals(Constants.STATUS_ACTIVE)){
            throw new RequestException(ErrorCode.CUSTOMER_ERROR_NOT_FOUND, "Customer not found.");
        }
        return customer;
    }

    private Product checkProduct(Long productId) {
        Product product = productRepository.findById(productId).orElse(null);
        if (product == null || !product.getStatus().equals(Constants.STATUS_ACTIVE)){
            throw new RequestException(ErrorCode.PRODUCT_NOT_FOUND, "Product not found.");
        }
        return product;
    }
}
