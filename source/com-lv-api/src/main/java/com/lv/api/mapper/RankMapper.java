package com.lv.api.mapper;

import com.lv.api.dto.rank.RankDto;
import com.lv.api.form.rank.CreateRankForm;
import com.lv.api.form.rank.UpdateRankForm;
import com.lv.api.storage.model.Rank;
import org.mapstruct.*;

import java.util.List;

@Mapper(
        componentModel = "spring",
        unmappedTargetPolicy = ReportingPolicy.IGNORE,
        nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE
)
public interface RankMapper {
    @Named("fromCreateRankFormToEntityMapper")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(source = "name", target = "name")
    @Mapping(source = "avatar", target = "avatar")
    @Mapping(source = "target", target = "target")
    Rank fromCreateRankFormToEntity(CreateRankForm createRankForm);

    @Named("fromRankEntityToRankDtoMapper")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(source = "id", target = "id")
    @Mapping(source = "name", target = "name")
    @Mapping(source = "avatar", target = "avatar")
    @Mapping(source = "target", target = "target")
    RankDto fromRankEntityToRankDto(Rank rank);

    @Named("fromListEntityToListRankDtoMapper")
    @IterableMapping(elementTargetType = RankDto.class, qualifiedByName = "fromRankEntityToRankDtoMapper")
    List<RankDto> fromListEntityToListRankDto(List<Rank> rankList);

    @Named("formUpdateRankFormToEntityMapper")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(source = "name", target = "name")
    @Mapping(source = "avatar", target = "avatar")
    @Mapping(source = "target", target = "target")
    void formUpdateRankFormToEntity(UpdateRankForm updateRankForm, @MappingTarget Rank rank);
}
