package com.lv.api.controller;

import com.lv.api.constant.Constants;
import com.lv.api.dto.ApiMessageDto;
import com.lv.api.dto.ErrorCode;
import com.lv.api.dto.ResponseListObj;
import com.lv.api.dto.account.LoginDto;
import com.lv.api.dto.store.StoreDto;
import com.lv.api.dto.store.VerifyStoreDto;
import com.lv.api.exception.RequestException;
import com.lv.api.form.account.LoginForm;
import com.lv.api.form.store.CreateStoreForm;
import com.lv.api.form.store.VerifyDeviceForm;
import com.lv.api.intercepter.MyAuthentication;
import com.lv.api.jwt.JWTUtils;
import com.lv.api.jwt.UserJwt;
import com.lv.api.mapper.StoreMapper;
import com.lv.api.storage.criteria.StoreCriteria;
import com.lv.api.storage.model.Account;
import com.lv.api.storage.model.Store;
import com.lv.api.storage.repository.StoreRepository;
import com.lv.api.utils.DateUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.MediaType;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.time.LocalDate;
import java.util.Date;
import java.util.List;
import java.util.Objects;

@RestController
@RequestMapping("/v1/store")
@CrossOrigin(origins = "*", allowedHeaders = "*")
@Slf4j
@RequiredArgsConstructor
public class StoreController extends ABasicController {
    private final StoreRepository storeRepository;
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

    @PostMapping(value = "/verify-device", produces = MediaType.APPLICATION_JSON_VALUE)
    public ApiMessageDto<VerifyStoreDto> verifyDevice(@Valid @RequestBody VerifyDeviceForm verifyDeviceForm, BindingResult bindingResult) {
        ApiMessageDto<VerifyStoreDto> apiMessageDto = new ApiMessageDto<>();
        Store store = storeRepository.findByPosId(verifyDeviceForm.getPosId());
        if (store == null || !verifyDeviceForm.getSessionId().equals(store.getSessionId()) || !Objects.equals(store.getStatus() , Constants.STATUS_ACTIVE)) {
            throw new RequestException(ErrorCode.STORE_ERROR_VERIFY, "Login fail, check your posId or sessionId");
        }
        VerifyStoreDto dto = new VerifyStoreDto();
        dto.setToken(generateJWT(store));
        dto.setId(store.getId());
        dto.setName(store.getName());

        apiMessageDto.setData(dto);
        apiMessageDto.setMessage("Login store success");
        return apiMessageDto;
    }

    private String generateJWT(Store store) {
        LocalDate parsedDate = LocalDate.now();
        parsedDate = parsedDate.plusDays(7);

        UserJwt qrJwt = new UserJwt();
        qrJwt.setPosId(store.getId());
        String appendStringRole = Constants.POS_DEVICE_PERMISSION;
        qrJwt.setDeviceId(store.getPosId());

        SecurityContext securityContext = SecurityContextHolder.getContext();
        securityContext.setAuthentication(new MyAuthentication(qrJwt));

        log.info("jwt user ne: {}", qrJwt);
        return JWTUtils.createJWT(JWTUtils.ALGORITHMS_HMAC, "authenticationToken.getId().toString()", qrJwt, DateUtils.convertToDateViaInstant(parsedDate));

    }

    @PostMapping(value = "/create", produces = MediaType.APPLICATION_JSON_VALUE)
    public ApiMessageDto<String> create(@Valid @RequestBody CreateStoreForm createStoreForm, BindingResult bindingResult) {
        if(!isAdmin()){
            throw new RequestException(ErrorCode.STORE_ERROR_UNAUTHORIZED, "Not allowed to create.");
        }
        ApiMessageDto<String> apiMessageDto = new ApiMessageDto<>();

        //check pos id unique
        Store store = storeRepository.findByPosId(createStoreForm.getPosId());
        if(store != null){
            throw new RequestException(ErrorCode.STORE_ERROR_BAD_REQUEST, "Store existed.");
        }

        store = storeMapper.fromCreateStoreFormToEntity(createStoreForm);
        storeRepository.save(store);
        apiMessageDto.setMessage("Create store success");
        return apiMessageDto;
    }

/*    @PutMapping(value = "/update", produces = MediaType.APPLICATION_JSON_VALUE)
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
    }*/

    @DeleteMapping(value = "/delete/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ApiMessageDto<String> delete(@PathVariable(name = "id") Long id) {
        Store store = storeRepository.findById(id)
                .orElseThrow(() -> new RequestException(ErrorCode.STORE_ERROR_NOT_FOUND, "Store not found"));
        storeRepository.delete(store);
        return new ApiMessageDto<>("Delete store successfully");
    }
}
