package com.amnex.rorsync.dto.response;

import com.amnex.rorsync.dto.request.FrFarmerFarmDto;
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
