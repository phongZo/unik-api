package com.lv.api.controller;

import com.lv.api.dto.ApiMessageDto;
import com.lv.api.dto.ErrorCode;
import com.lv.api.dto.ResponseListObj;
import com.lv.api.dto.variant.VariantDto;
import com.lv.api.exception.RequestException;
import com.lv.api.form.variant.CreateVariantForm;
import com.lv.api.form.variant.UpdateVariantForm;
import com.lv.api.mapper.VariantMapper;
import com.lv.api.storage.criteria.VariantCriteria;
import com.lv.api.storage.model.Variant;
import com.lv.api.storage.repository.VariantRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.MediaType;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/v1/variant")
@CrossOrigin(origins = "*", allowedHeaders = "*")
@Slf4j
@RequiredArgsConstructor
public class VariantController extends ABasicController {
    private final VariantRepository variantRepository;
    private final VariantMapper variantMapper;

    @GetMapping(value = "/list", produces = MediaType.APPLICATION_JSON_VALUE)
    public ApiMessageDto<ResponseListObj<VariantDto>> list(VariantCriteria variantCriteria, Pageable pageable) {
        Page<Variant> variantPage = variantRepository.findAll(variantCriteria.getSpecification(), pageable);
        List<VariantDto> variantDtoList = variantMapper.fromVariantEntityListToDtoList(variantPage.getContent());
        return new ApiMessageDto<>(new ResponseListObj<>(variantDtoList, variantPage), "Get list successfully");
    }

    @GetMapping(value = "/auto-complete", produces = MediaType.APPLICATION_JSON_VALUE)
    public ApiMessageDto<List<VariantDto>> autoComplete(VariantCriteria variantCriteria) {
        List<Variant> variantDtoList = variantRepository.findAll(variantCriteria.getSpecification(), Sort.by(Sort.Order.asc("name")));
        return new ApiMessageDto<>(
                variantMapper.fromVariantEntityListToDtoList(variantDtoList),
                "Get list success"
        );
    }

    @GetMapping(value = "/get/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ApiMessageDto<VariantDto> get(@PathVariable(name = "id") Long id) {
        Variant variant = variantRepository.findById(id)
                .orElseThrow(() -> new RequestException(ErrorCode.VARIANT_NOT_FOUND, "Variant not found"));
        return new ApiMessageDto<>(variantMapper.fromVariantEntityToDto(variant), "Get variant successfully");
    }

    @PostMapping(value = "/create", produces = MediaType.APPLICATION_JSON_VALUE)
    public ApiMessageDto<String> create(@Valid @RequestBody CreateVariantForm createVariantForm, BindingResult bindingResult) {
        Variant variant = variantMapper.fromCreateVariantFormToEntity(createVariantForm);
        variantRepository.save(variant);
        return new ApiMessageDto<>("Create variant successfully");
    }

    @PutMapping(value = "/update", produces = MediaType.APPLICATION_JSON_VALUE)
    public ApiMessageDto<String> update(@Valid @RequestBody UpdateVariantForm updateVariantForm, BindingResult bindingResult) {
        Variant variant = variantRepository.findById(updateVariantForm.getId())
                .orElseThrow(() -> new RequestException(ErrorCode.VARIANT_NOT_FOUND, "Variant not found"));
        variantMapper.fromUpdateVariantFormToEntity(updateVariantForm, variant);
        variantRepository.save(variant);
        return new ApiMessageDto<>("Update variant successfully");
    }

    @DeleteMapping(value = "/delete/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ApiMessageDto<String> delete(@PathVariable(name = "id") Long id) {
        Variant variant = variantRepository.findById(id)
                .orElseThrow(() -> new RequestException(ErrorCode.VARIANT_NOT_FOUND, "Variant not found"));
        variantRepository.delete(variant);
        return new ApiMessageDto<>("Delete variant successfully");
    }
}
