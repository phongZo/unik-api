package com.lv.api.controller;

import com.lv.api.dto.ApiMessageDto;
import com.lv.api.dto.ErrorCode;
import com.lv.api.dto.ResponseListObj;
import com.lv.api.dto.store.StoreDto;
import com.lv.api.exception.RequestException;
import com.lv.api.form.store.CreateStoreForm;
import com.lv.api.form.store.UpdateStoreForm;
import com.lv.api.mapper.StoreMapper;
import com.lv.api.storage.criteria.StoreCriteria;
import com.lv.api.storage.model.Location;
import com.lv.api.storage.model.Store;
import com.lv.api.storage.repository.LocationRepository;
import com.lv.api.storage.repository.StoreRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.MediaType;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;
import java.util.Objects;

@RestController
@RequestMapping("/v1/store")
@CrossOrigin(origins = "*", allowedHeaders = "*")
@Slf4j
@RequiredArgsConstructor
public class StoreController extends ABasicController {
    private final StoreRepository storeRepository;
    private final LocationRepository locationRepository;
    private final StoreMapper storeMapper;

    @GetMapping(value = "/list", produces = MediaType.APPLICATION_JSON_VALUE)
    public ApiMessageDto<ResponseListObj<StoreDto>> list(StoreCriteria storeCriteria, Pageable pageable) {
        Page<Store> storePage = storeRepository.findAll(storeCriteria.getSpecification(), pageable);
        List<StoreDto> storeDtoList = storeMapper.fromStoreEntityListToDtoList(storePage.getContent());
        return new ApiMessageDto<>(new ResponseListObj<>(storeDtoList, storePage), "Get list successfully");
    }

    @GetMapping(value = "/auto-complete", produces = MediaType.APPLICATION_JSON_VALUE)
    public ApiMessageDto<List<StoreDto>> autoComplete(StoreCriteria storeCriteria) {
        Page<Store> storePage = storeRepository.findAll(storeCriteria.getSpecification(), Pageable.unpaged());
        return new ApiMessageDto<>(
                storeMapper.fromStoreEntityListToDtoList(storePage.getContent()),
                "Get list successfully"
        );
    }

    @GetMapping(value = "/get/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ApiMessageDto<StoreDto> get(@PathVariable(name = "id") Long id) {
        Store store = storeRepository.findById(id)
                .orElseThrow(() -> new RequestException(ErrorCode.STORE_ERROR_NOT_FOUND, "Store not found"));
        return new ApiMessageDto<>(storeMapper.fromStoreEntityToDto(store), "Get store successfully");
    }

    @PostMapping(value = "/create", produces = MediaType.APPLICATION_JSON_VALUE)
    public ApiMessageDto<String> create(@Valid @RequestBody CreateStoreForm createStoreForm, BindingResult bindingResult) {
        Location ward = locationRepository.findById(createStoreForm.getWardId())
                .orElseThrow(() -> new RequestException(ErrorCode.LOCATION_ERROR_NOTFOUND, "Ward not found"));
        Location district = ward.getParent();
        Location province = district.getParent();
        if (!Objects.equals(district.getId(), createStoreForm.getDistrictId()))
            throw new RequestException(ErrorCode.LOCATION_ERROR_INVALID, "Invalid district");
        if (!Objects.equals(province.getId(), createStoreForm.getProvinceId()))
            throw new RequestException(ErrorCode.LOCATION_ERROR_INVALID, "Invalid province");
        Store store = storeMapper.fromCreateStoreFormToEntity(createStoreForm);
        store.setProvince(province);
        store.setDistrict(district);
        store.setWard(ward);
        storeRepository.save(store);
        return new ApiMessageDto<>("Create store successfully");
    }

    @PutMapping(value = "/update", produces = MediaType.APPLICATION_JSON_VALUE)
    public ApiMessageDto<String> update(@Valid @RequestBody UpdateStoreForm updateStoreForm, BindingResult bindingResult) {
        Store store = storeRepository.findById(updateStoreForm.getId())
                .orElseThrow(() -> new RequestException(ErrorCode.STORE_ERROR_NOT_FOUND, "Store not found"));
        Location ward = locationRepository.findById(updateStoreForm.getWardId())
                .orElseThrow(() -> new RequestException(ErrorCode.LOCATION_ERROR_NOTFOUND, "Ward not found"));
        Location district = ward.getParent();
        Location province = district.getParent();
        if (!Objects.equals(district.getId(), updateStoreForm.getDistrictId()))
            throw new RequestException(ErrorCode.LOCATION_ERROR_INVALID, "Invalid district");
        if (!Objects.equals(province.getId(), updateStoreForm.getProvinceId()))
            throw new RequestException(ErrorCode.LOCATION_ERROR_INVALID, "Invalid province");
        storeMapper.fromUpdateStoreFormToEntity(updateStoreForm, store);
        store.setProvince(province);
        store.setDistrict(district);
        store.setWard(ward);
        storeRepository.save(store);
        return new ApiMessageDto<>("Update store successfully");
    }

    @DeleteMapping(value = "/delete/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ApiMessageDto<String> delete(@PathVariable(name = "id") Long id) {
        Store store = storeRepository.findById(id)
                .orElseThrow(() -> new RequestException(ErrorCode.STORE_ERROR_NOT_FOUND, "Store not found"));
        storeRepository.delete(store);
        return new ApiMessageDto<>("Delete store successfully");
    }
}
