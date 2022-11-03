package com.lv.api.controller;

import com.lv.api.constant.Constants;
import com.lv.api.dto.ApiMessageDto;
import com.lv.api.dto.ErrorCode;
import com.lv.api.dto.ResponseListObj;
import com.lv.api.dto.product.ProductAdminDto;
import com.lv.api.dto.product.ProductDto;
import com.lv.api.exception.RequestException;
import com.lv.api.form.product.CreateProductForm;
import com.lv.api.form.product.UpdateProductForm;
import com.lv.api.mapper.ProductMapper;
import com.lv.api.service.CommonApiService;
import com.lv.api.storage.criteria.ProductCriteria;
import com.lv.api.storage.model.Product;
import com.lv.api.storage.model.ProductCategory;
import com.lv.api.storage.repository.ProductCategoryRepository;
import com.lv.api.storage.repository.ProductRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.MediaType;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.transaction.Transactional;
import javax.validation.Valid;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/v1/product")
@CrossOrigin(origins = "*", allowedHeaders = "*")
@Slf4j
@RequiredArgsConstructor
public class ProductController extends ABasicController {
    private final ProductRepository productRepository;
    private final ProductCategoryRepository productCategoryRepository;
    private final ProductMapper productMapper;
    private final CommonApiService commonApiService;

    @GetMapping(value = "/list", produces = MediaType.APPLICATION_JSON_VALUE)
    public ApiMessageDto<ResponseListObj<ProductAdminDto>> list(@Valid ProductCriteria productCriteria, BindingResult bindingResult, Pageable pageable) {
        Page<Product> productPage = productRepository.findAll(productCriteria.getSpecification(), pageable);
        List<ProductAdminDto> productAdminDtoList = productMapper.fromProductEntityListToAdminDtoList(productPage.getContent());
        return new ApiMessageDto<>(
                new ResponseListObj<>(
                        productAdminDtoList,
                        productPage
                ),
                "Get list product successfully"
        );
    }

    @GetMapping(value = "/auto-complete", produces = MediaType.APPLICATION_JSON_VALUE)
    public ApiMessageDto<List<ProductDto>> autoComplete(@Valid ProductCriteria productCriteria) {
        Page<Product> productPage = productRepository.findAll(productCriteria.getSpecification(), Pageable.unpaged());
        List<ProductDto> productDtoList = productMapper.fromProductEntityListToDtoListAutoComplete(productPage.getContent());
        return new ApiMessageDto<>(productDtoList, "Get list successfully");
    }

    @GetMapping(value = "/get/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ApiMessageDto<ProductAdminDto> get(@PathVariable(name = "id") Long id) {
        Product product = productRepository.findById(id)
                .orElseThrow(() -> new RequestException(ErrorCode.PRODUCT_NOT_FOUND, "Product not found"));
        ProductAdminDto productAdminDto = productMapper.fromProductEntityToAdminDto(product);
        return new ApiMessageDto<>(productAdminDto, "Get product successfully");
    }

    @Transactional
    @PostMapping(value = "/create", produces = MediaType.APPLICATION_JSON_VALUE)
    public ApiMessageDto<String> create(@Valid @RequestBody CreateProductForm createProductForm, BindingResult bindingResult) {
        Product product = productMapper.fromCreateProductFormToEntity(createProductForm);
        if (createProductForm.getCategoryId() != null) {
            ProductCategory category = productCategoryRepository.findById(createProductForm.getCategoryId())
                    .orElseThrow(() -> new RequestException(ErrorCode.PRODUCT_CATEGORY_ERROR_NOT_FOUND, "Product category not found"));
            product.setCategory(category);
        }
        if (createProductForm.getParentProductId() != null) {
            Product parentProduct = productRepository.findById(createProductForm.getParentProductId())
                    .orElseThrow(() -> new RequestException(ErrorCode.PRODUCT_NOT_FOUND, "Parent product not found"));
            if (!parentProduct.getKind().equals(Constants.PRODUCT_KIND_GROUP)) {
                parentProduct.setKind(Constants.PRODUCT_KIND_GROUP);
                parentProduct = productRepository.saveAndFlush(parentProduct);
            }
            product.setParentProduct(parentProduct);
        }
        productRepository.save(product);
        return new ApiMessageDto<>("Create product successfully");
    }

    @Transactional
    @PutMapping(value = "/update", produces = MediaType.APPLICATION_JSON_VALUE)
    public ApiMessageDto<String> update(@Valid @RequestBody UpdateProductForm updateProductForm, BindingResult bindingResult) {
        Product product = productRepository.findById(updateProductForm.getId())
                .orElseThrow(() -> new RequestException(ErrorCode.PRODUCT_NOT_FOUND, "Product not found"));
        Map<Long, String> imageMap = new HashMap<>();
        for (var productConfig : product.getProductConfigs()) {
            for (var productVariant : productConfig.getVariants()) {
                if (productVariant.getImage() != null)
                    imageMap.put(productVariant.getId(), productVariant.getImage());
            }
        }
        for (var productConfig : updateProductForm.getProductConfigs()) {
            for (var productVariant : productConfig.getVariants()) {
                if (productVariant.getId() != null) {
                    String image = imageMap.get(productVariant.getId());
                    if (image != null && !image.equals(productVariant.getImage()))
                        commonApiService.deleteFile(image);
                }
            }
        }
        if (updateProductForm.getCategoryId() != null) {
            ProductCategory category = productCategoryRepository.findById(updateProductForm.getCategoryId())
                    .orElseThrow(() -> new RequestException(ErrorCode.PRODUCT_CATEGORY_ERROR_NOT_FOUND, "Product category not found"));
            product.setCategory(category);
        } else {
            product.setCategory(null);
        }

        if (updateProductForm.getParentProductId() != null) {
            Product parentProduct = productRepository.findById(updateProductForm.getParentProductId())
                    .orElseThrow(() -> new RequestException(ErrorCode.PRODUCT_NOT_FOUND, "Parent product not found"));
            if (!parentProduct.getKind().equals(Constants.PRODUCT_KIND_GROUP)) {
                parentProduct.setKind(Constants.PRODUCT_KIND_GROUP);
                parentProduct = productRepository.saveAndFlush(parentProduct);
            }
            product.setParentProduct(parentProduct);
        } else {
            product.setParentProduct(null);
        }
        productMapper.fromUpdateProductFormToEntity(updateProductForm, product);
        productRepository.save(product);
        return new ApiMessageDto<>("Update product successfully");
    }

    @DeleteMapping(value = "/delete/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ApiMessageDto<String> delete(@PathVariable(name = "id") Long id) {
        Product product = productRepository.findById(id)
                .orElseThrow(() -> new RequestException(ErrorCode.PRODUCT_NOT_FOUND, "Product not found"));

        productRepository.delete(product);
        return new ApiMessageDto<>("Delete product successfully");
    }
}
