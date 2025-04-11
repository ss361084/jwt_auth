package com.amnex.rorsync.mapper;

import com.amnex.rorsync.dto.FrFarmerFarmUniqueDto;
import com.amnex.rorsync.dto.request.FrFarmerFarmDto;
import com.amnex.rorsync.dto.response.FrFarmerFarmResDto;
import com.amnex.rorsync.entity.FrFarmerFarmData;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Mappings;

@Mapper(componentModel = "spring")
public interface FrFarmerFarmMapper {

    FrFarmerFarmData toEntity(FrFarmerFarmDto rorSyncDto);

    FrFarmerFarmDto toDto(FrFarmerFarmData rorSyncData);

    @Mappings({
            @Mapping(source = "id",target = "rorResponseId")
    })
    FrFarmerFarmResDto toResposneDto (FrFarmerFarmData rorSyncData);

    FrFarmerFarmUniqueDto toUniqueDto(FrFarmerFarmDto rorSyncDto);
}
