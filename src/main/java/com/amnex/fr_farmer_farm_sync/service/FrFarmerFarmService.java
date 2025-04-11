package com.amnex.fr_farmer_farm_sync.service;

import com.amnex.fr_farmer_farm_sync.dto.request.FrFarmerFarmDto;
import com.amnex.fr_farmer_farm_sync.dto.response.ResponseModel;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

public interface FrFarmerFarmService {

    public ResponseModel pushRorData(List<FrFarmerFarmDto> frFarmerFarmDtoList, HttpServletRequest request);
}
