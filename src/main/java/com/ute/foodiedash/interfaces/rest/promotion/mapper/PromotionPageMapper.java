package com.ute.foodiedash.interfaces.rest.promotion.mapper;

import com.ute.foodiedash.application.promotion.query.PromotionPageResult;
import com.ute.foodiedash.interfaces.rest.common.dto.PageInfo;
import com.ute.foodiedash.interfaces.rest.promotion.dto.PromotionResponseDTO;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring", uses = PromotionDtoMapper.class)
public interface PromotionPageMapper {

    @Mapping(target = "pageContent", source = "content")
    @Mapping(target = "hasNextPage", source = "hasNextPage")
    @Mapping(target = "hasPreviousPage", source = "hasPreviousPage")
    PageInfo<PromotionResponseDTO> toPageInfo(PromotionPageResult result);
}
