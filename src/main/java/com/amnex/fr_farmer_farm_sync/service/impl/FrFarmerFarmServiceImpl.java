package com.amnex.fr_farmer_farm_sync.service.impl;

import com.amnex.fr_farmer_farm_sync.dto.FrFarmerFarmUniqueDto;
import com.amnex.fr_farmer_farm_sync.dto.request.FrFarmerFarmDto;
import com.amnex.fr_farmer_farm_sync.dto.response.FrFarmerFarmResDto;
import com.amnex.fr_farmer_farm_sync.dto.response.FrFarmerFarmResponseDto;
import com.amnex.fr_farmer_farm_sync.dto.response.ResponseModel;
import com.amnex.fr_farmer_farm_sync.entity.FrFarmerFarmData;
import com.amnex.fr_farmer_farm_sync.mapper.FrFarmerFarmMapper;
import com.amnex.fr_farmer_farm_sync.repository.FrFarmerFarmDataRepository;
import com.amnex.fr_farmer_farm_sync.service.FrFarmerFarmService;
import com.amnex.fr_farmer_farm_sync.util.CommonUtils;
import com.amnex.fr_farmer_farm_sync.util.Constants;
import com.amnex.fr_farmer_farm_sync.util.JdbcUtil;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.servlet.http.HttpServletRequest;
import java.sql.Timestamp;
import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.function.BiFunction;
import java.util.stream.Collectors;

@Service
@Transactional
public class FrFarmerFarmServiceImpl implements FrFarmerFarmService {

    @Autowired
    private FrFarmerFarmMapper frFarmerFarmMapper;

    @Autowired
    private CommonUtils commonUtils;

    @Autowired
    private FrFarmerFarmDataRepository frFarmerFarmDataRepository;

    @Autowired
    private JdbcUtil jdbcUtil;

    BiFunction<List<FrFarmerFarmUniqueDto>, FrFarmerFarmDto, Boolean> FrFarmerFarmDataIsExists = (list, syncData) -> list.stream().anyMatch(u -> u.getFarmerId().equals(syncData.getFarmerId()) && u.getFarmId().equals(syncData.getFarmId()));

    @Override
    public ResponseModel pushRorData(List<FrFarmerFarmDto> FrFarmerFarmDtoList, HttpServletRequest request) {
        ResponseModel model = new ResponseModel();
        try {
            if(FrFarmerFarmDtoList.isEmpty()) {
                model.setMessage(Constants.No_DATA_FOUND);
                model.setCode(Constants.NOT_FOUND_CODE);
            } else {
                String userId = commonUtils.getUserId(request);
                List<FrFarmerFarmResDto> responseDtos = new ArrayList<>();
                List<FrFarmerFarmDto> duplicateRecords = new ArrayList<>();
                final AtomicInteger counter = new AtomicInteger();
                Collection<List<FrFarmerFarmDto>> result = FrFarmerFarmDtoList.stream().collect(Collectors.groupingBy(it -> counter.getAndIncrement() / 100)).values();
                result.parallelStream().forEach(syncData -> {
                    // check duplication records
                    String combiningRecords = syncData.stream()
                        .map(frFarmerFarmMapper::toUniqueDto)
                        .map(entry -> String.format("('%s','%s')", entry.getFarmerId(), entry.getFarmId()))
                        .collect(Collectors.joining(", "));
                    if(StringUtils.isNotEmpty(combiningRecords)) {
                        String combineRecords = jdbcUtil.findExistingRolesUsingJdbc(combiningRecords);
                        List<FrFarmerFarmUniqueDto> duplicateList = commonUtils.convertJsonToList(combineRecords, FrFarmerFarmUniqueDto.class);
                        if(!duplicateList.isEmpty()) {
                            syncData = syncData.stream().filter(rec -> {
                                boolean recordsFound = FrFarmerFarmDataIsExists.apply(duplicateList,rec);
                                if(recordsFound) {
                                    duplicateRecords.add(rec);
                                }
                                return !recordsFound;
                            }).collect(Collectors.toList());
                        }
                    }
                    if(!syncData.isEmpty()) {
                        List<FrFarmerFarmData> syncFinalData = syncData.stream()
                                .map(frFarmerFarmMapper::toEntity)
                                .map(e -> {
                                    e.setCreatedBy(userId);
                                    e.setCreatedOn(new Timestamp(new Date().getTime()));
                                    e.setModifiedBy(userId);
                                    e.setModifiedOn(new Timestamp(new Date().getTime()));
                                    return e;
                                })
                                .collect(Collectors.toList());
                        syncFinalData = frFarmerFarmDataRepository.saveAll(syncFinalData);
                        List<FrFarmerFarmResDto> responseList = syncFinalData.stream().map(frFarmerFarmMapper::toResposneDto).collect(Collectors.toList());
                        if(!responseList.isEmpty()) {
                            responseDtos.addAll(responseList);
                        }
                    }
                });
                FrFarmerFarmResponseDto finalResponse = new FrFarmerFarmResponseDto();
                finalResponse.setPushedRecords(responseDtos);
                finalResponse.setDuplicateRecords(duplicateRecords);

                model.setData(finalResponse);
                model.setCode(Constants.SUCCESS_CODE);
                model.setMessage(Constants.SUCCESS);
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return model;
    }
}
