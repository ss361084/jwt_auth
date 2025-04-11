package com.amnex.rorsync.controller;

import com.amnex.rorsync.dto.request.FrFarmerFarmDto;
import com.amnex.rorsync.dto.response.ResponseModel;
import com.amnex.rorsync.service.FrFarmerFarmService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

@RestController
@RequestMapping("/ror-sync")
public class RorSyncController {

    @Autowired
    private FrFarmerFarmService rorSyncService;

    @PostMapping(value = "/push")
    public ResponseModel pushRorRecords(@RequestBody List<FrFarmerFarmDto> rorSyncDtoList, HttpServletRequest request) {
        return rorSyncService.pushRorData(rorSyncDtoList,request);
    }
}
