package com.amnex.rorsync.service;

import com.amnex.rorsync.dto.request.FrFarmerFarmDto;
import com.amnex.rorsync.dto.response.ResponseModel;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

public interface FrFarmerFarmService {

    public ResponseModel pushRorData(List<FrFarmerFarmDto> rorSyncDtoList, HttpServletRequest request);
}
