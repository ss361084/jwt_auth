package com.amnex.fr_farmer_farm_sync.controller;

import com.amnex.fr_farmer_farm_sync.dto.request.FrFarmerFarmDto;
import com.amnex.fr_farmer_farm_sync.dto.response.ResponseModel;
import com.amnex.fr_farmer_farm_sync.service.FrFarmerFarmService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

@RestController
@RequestMapping("/fr-farmer-farm")
public class FrFarmerFarmController {

    @Autowired
    private FrFarmerFarmService frFarmerFarmService;

    @PostMapping(value = "/push")
    public ResponseModel pushRorRecords(@RequestBody List<FrFarmerFarmDto> frFarmerFarmDtoList, HttpServletRequest request) {
        return frFarmerFarmService.pushRorData(frFarmerFarmDtoList,request);
    }
}
