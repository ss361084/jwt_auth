package com.amnex.rorsync.mapper;

import com.amnex.rorsync.dto.RorSyncUniqueDto;
import com.amnex.rorsync.dto.request.RorSyncDto;
import com.amnex.rorsync.dto.response.RorSyncResDto;
import com.amnex.rorsync.entity.RorSyncData;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Mappings;

@Mapper(componentModel = "spring")
public interface RorSyncMapper {

    RorSyncData toEntity(RorSyncDto rorSyncDto);

    RorSyncDto toDto(RorSyncData rorSyncData);

    @Mappings({
            @Mapping(source = "id",target = "rorResponseId")
    })
    RorSyncResDto toResposneDto (RorSyncData rorSyncData);

    RorSyncUniqueDto toUniqueDto(RorSyncDto rorSyncDto);
}
