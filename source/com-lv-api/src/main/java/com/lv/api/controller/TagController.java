package com.lv.api.controller;

import com.lv.api.dto.ApiMessageDto;
import com.lv.api.dto.ErrorCode;
import com.lv.api.dto.ResponseListObj;
import com.lv.api.dto.tag.TagDto;
import com.lv.api.exception.RequestException;
import com.lv.api.form.tag.CreateTagForm;
import com.lv.api.form.tag.UpdateTagForm;
import com.lv.api.mapper.TagMapper;
import com.lv.api.storage.criteria.TagCriteria;
import com.lv.api.storage.model.Tag;
import com.lv.api.storage.repository.TagRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.MediaType;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/v1/tag")
@CrossOrigin(origins = "*", allowedHeaders = "*")
@Slf4j
@RequiredArgsConstructor
public class TagController extends ABasicController {
    private final TagRepository tagRepository;
    private final TagMapper tagMapper;

    @GetMapping(value = "/list", produces = MediaType.APPLICATION_JSON_VALUE)
    public ApiMessageDto<ResponseListObj<TagDto>> list(TagCriteria tagCriteria, Pageable pageable) {
        Page<Tag> tagPage = tagRepository.findAll(tagCriteria.getSpecification(), pageable);
        List<TagDto> tagDtoList = tagMapper.fromTagEntityListToDtoList(tagPage.getContent());
        return new ApiMessageDto<>(
                new ResponseListObj<>(tagDtoList, tagPage),
                "Get list successfully"
        );
    }

    @GetMapping(value = "/auto-complete", produces = MediaType.APPLICATION_JSON_VALUE)
    public ApiMessageDto<List<TagDto>> autoComplete(TagCriteria tagCriteria) {
        List<Tag> tagList = tagRepository.findAll(tagCriteria.getSpecificationAutoComplete());
        return new ApiMessageDto<>(tagMapper.fromTagEntityListToDtoList(tagList), "Get list successfully");
    }

    @GetMapping(value = "/get/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ApiMessageDto<TagDto> get(@PathVariable(name = "id") Long id) {
        Tag tag = tagRepository.findById(id)
                .orElseThrow(() -> new RequestException(ErrorCode.TAG_NOT_FOUND, "Tag not found"));
        return new ApiMessageDto<>(tagMapper.fromTagEntityToDto(tag), "Get tag successfully");
    }

    @PostMapping(value = "/create", produces = MediaType.APPLICATION_JSON_VALUE)
    public ApiMessageDto<String> create(@Valid @RequestBody CreateTagForm createTagForm, BindingResult bindingResult) {
        if (tagRepository.countByTag(createTagForm.getTag()) > 0) {
            throw new RequestException(ErrorCode.TAG_EXISTED, String.format("Tag %s existed", createTagForm.getTag()));
        }
        Tag tag = tagMapper.fromCreateTagFromToEntity(createTagForm);
        tagRepository.save(tag);
        return new ApiMessageDto<>("Create tag successfully");
    }

    @PutMapping(value = "/update", produces = MediaType.APPLICATION_JSON_VALUE)
    public ApiMessageDto<String> update(@Valid @RequestBody UpdateTagForm updateTagForm, BindingResult bindingResult) {
        if (tagRepository.countByTagAndIdNot(updateTagForm.getTag(), updateTagForm.getId()) > 0) {
            throw new RequestException(ErrorCode.TAG_EXISTED, String.format("Tag %s existed", updateTagForm.getTag()));
        }
        Tag tag = tagRepository.findById(updateTagForm.getId())
                .orElseThrow(() -> new RequestException(ErrorCode.TAG_NOT_FOUND, "Tag not found"));
        tagMapper.fromUpdateTagFromToEntity(updateTagForm, tag);
        tagRepository.save(tag);
        return new ApiMessageDto<>("Update tag successfully");
    }

    @DeleteMapping(value = "/delete/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ApiMessageDto<String> delete(@PathVariable(name = "id") Long id) {
        Tag tag = tagRepository.findById(id)
                .orElseThrow(() -> new RequestException(ErrorCode.TAG_NOT_FOUND, "Tag not found"));
        tagRepository.delete(tag);
        return new ApiMessageDto<>("Delete tag successfully");
    }
}
