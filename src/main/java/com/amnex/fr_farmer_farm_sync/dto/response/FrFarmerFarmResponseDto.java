package com.amnex.fr_farmer_farm_sync.dto.response;

import com.amnex.fr_farmer_farm_sync.dto.request.FrFarmerFarmDto;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class FrFarmerFarmResponseDto {
    private List<FrFarmerFarmResDto> pushedRecords;
    private List<FrFarmerFarmDto> duplicateRecords;
}
