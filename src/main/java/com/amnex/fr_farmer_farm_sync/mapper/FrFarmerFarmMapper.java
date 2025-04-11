package com.amnex.fr_farmer_farm_sync.mapper;

import com.amnex.fr_farmer_farm_sync.dto.FrFarmerFarmUniqueDto;
import com.amnex.fr_farmer_farm_sync.dto.request.FrFarmerFarmDto;
import com.amnex.fr_farmer_farm_sync.dto.response.FrFarmerFarmResDto;
import com.amnex.fr_farmer_farm_sync.entity.FrFarmerFarmData;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Mappings;

@Mapper(componentModel = "spring")
public interface FrFarmerFarmMapper {

    FrFarmerFarmData toEntity(FrFarmerFarmDto frFarmerFarmDto);

    FrFarmerFarmDto toDto(FrFarmerFarmData frFarmerFarmData);

    @Mappings({
            @Mapping(source = "id",target = "rorResponseId")
    })
    FrFarmerFarmResDto toResposneDto (FrFarmerFarmData frFarmerFarmData);

    FrFarmerFarmUniqueDto toUniqueDto(FrFarmerFarmDto frFarmerFarmDto);
}
