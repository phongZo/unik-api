package com.lv.api.controller;

import com.lv.api.dto.ApiMessageDto;
import com.lv.api.dto.ErrorCode;
import com.lv.api.dto.ResponseListObj;
import com.lv.api.dto.location.LocationDto;
import com.lv.api.exception.RequestException;
import com.lv.api.form.location.CreateLocationForm;
import com.lv.api.form.location.UpdateLocationForm;
import com.lv.api.mapper.LocationMapper;
import com.lv.api.storage.criteria.LocationCriteria;
import com.lv.api.storage.model.Location;
import com.lv.api.storage.repository.LocationRepository;
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
@RequestMapping("/v1/locations")
@CrossOrigin(origins = "*", allowedHeaders = "*")
@Slf4j
@RequiredArgsConstructor
public class LocationController extends ABasicController {
    private final LocationRepository locationRepository;
    private final LocationMapper locationMapper;

    @GetMapping(value = "/list", produces = MediaType.APPLICATION_JSON_VALUE)
    public ApiMessageDto<ResponseListObj<LocationDto>> list(@Valid LocationCriteria locationCriteria, BindingResult bindingResult, Pageable pageable) {
        ApiMessageDto<ResponseListObj<LocationDto>> responseListObjApiMessageDto = new ApiMessageDto<>();
        Page<Location> listLocation = locationRepository.findAll(locationCriteria.getSpecification(), pageable);
        ResponseListObj<LocationDto> responseListObj = new ResponseListObj<>();
        responseListObj.setData(locationMapper.fromEntityListToLocationDtoList(listLocation.getContent()));
        responseListObj.setPage(listLocation.getNumber());
        responseListObj.setTotalPage(listLocation.getTotalPages());
        responseListObj.setTotalElements(listLocation.getTotalElements());

        responseListObjApiMessageDto.setData(responseListObj);
        responseListObjApiMessageDto.setMessage("Get list success");
        return responseListObjApiMessageDto;
    }

    @GetMapping(value = "auto-complete", produces = MediaType.APPLICATION_JSON_VALUE)
    public ApiMessageDto<List<LocationDto>> autoComplete(LocationCriteria locationCriteria) {
        List<Location> listLocation = locationRepository.findAll(locationCriteria.getSpecificationAutoComplete(), Sort.by(Sort.Order.asc("name")));
        return new ApiMessageDto<>(
                locationMapper.fromEntityListToLocationDtoListAutoComplete(listLocation),
                "Get list success"
        );
    }

    @GetMapping(value = "/get/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ApiMessageDto<LocationDto> getLocation(@PathVariable("id") Long id) {
        Location location = locationRepository.findLocationById(id)
                .orElseThrow(() -> new RequestException(ErrorCode.LOCATION_ERROR_NOTFOUND, "Location not found"));
        return new ApiMessageDto<>(locationMapper.fromEntityToLocationDto(location), "Get location successfully");
    }

    @PostMapping(value = "/create", produces = MediaType.APPLICATION_JSON_VALUE)
    public ApiMessageDto<String> create(@Valid @RequestBody CreateLocationForm createLocationForm, BindingResult bindingResult) {
        Location parentLocation = null;
        if (createLocationForm.getParentId() != null) {
            parentLocation = locationRepository.findLocationById(createLocationForm.getParentId())
                    .orElseThrow(() -> new RequestException(ErrorCode.LOCATION_ERROR_NOTFOUND, "Location not found"));
            if (parentLocation.getKind() - createLocationForm.getKind() != -1)
                throw new RequestException(ErrorCode.LOCATION_ERROR_INVALID_PARENT, "Invalid parent location");
        }
        Location location = locationMapper.fromCreateLocationFromToEntity(createLocationForm, parentLocation);
        locationRepository.save(location);
        return new ApiMessageDto<>("Create location successfully");
    }

    @PutMapping(value = "/update", produces = MediaType.APPLICATION_JSON_VALUE)
    public ApiMessageDto<String> update(@Valid @RequestBody UpdateLocationForm updateLocationForm, BindingResult bindingResult) {
        Location location = locationRepository.findLocationById(updateLocationForm.getId())
                .orElseThrow(() -> new RequestException(ErrorCode.LOCATION_ERROR_NOTFOUND, "Location not found"));
        locationMapper.fromUpdateLocationFormToEntity(updateLocationForm, location);
        locationRepository.save(location);
        return new ApiMessageDto<>("Update location successfully");
    }

    @DeleteMapping(value = "/delete/{id}")
    public ApiMessageDto<String> delete(@PathVariable("id") Long id) {
        Location location = locationRepository.findLocationById(id)
                .orElseThrow(() -> new RequestException(ErrorCode.LOCATION_ERROR_NOTFOUND, "Location not found"));
        locationRepository.delete(location);
        return new ApiMessageDto<>("Delete location successfully");
    }
}
