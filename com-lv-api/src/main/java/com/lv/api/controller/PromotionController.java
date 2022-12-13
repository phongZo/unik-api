package com.lv.api.controller;

import com.lv.api.constant.Constants;
import com.lv.api.dto.ApiMessageDto;
import com.lv.api.dto.ErrorCode;
import com.lv.api.exception.RequestException;
import com.lv.api.form.promotion.CreatePromotionForm;
import com.lv.api.mapper.PromotionMapper;
import com.lv.api.storage.model.Promotion;
import com.lv.api.storage.repository.PromotionRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
@RequestMapping("/v1/promotion")
@CrossOrigin(origins = "*", allowedHeaders = "*")
@Slf4j
public class PromotionController extends ABasicController{
    @Autowired
    PromotionMapper promotionMapper;

    @Autowired
    PromotionRepository promotionRepository;

    @PostMapping(value = "/create", produces = MediaType.APPLICATION_JSON_VALUE)
    public ApiMessageDto<String> create(@Valid @RequestBody CreatePromotionForm createPromotionForm, BindingResult bindingResult) {
        if(!isAdmin()){
            throw new RequestException(ErrorCode.PROMOTION_ERROR_UNAUTHORIZED, "Not allowed to create.");
        }
        ApiMessageDto<String> apiMessageDto = new ApiMessageDto<>();
        Promotion promotion = promotionMapper.fromCreateFormToEntity(createPromotionForm);
        if(promotion.getKind().equals(Constants.PROMOTION_KIND_PERCENT)){
            Integer value = Integer.valueOf(promotion.getValue());
            if(value < 0 || value > 100){
                throw new RequestException(ErrorCode.PROMOTION_ERROR_BAD_REQUEST, "Value invalid.");
            }
        } else{
            if(promotion.getMaxValueForPercent() != null){
                throw new RequestException(ErrorCode.PROMOTION_ERROR_BAD_REQUEST, "Kind money not have max value.");
            }
        }
        promotionRepository.save(promotion);
        apiMessageDto.setMessage("Create promotion success");
        return apiMessageDto;
    }
}
