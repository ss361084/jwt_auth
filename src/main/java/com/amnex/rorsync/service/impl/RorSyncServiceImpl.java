package com.amnex.rorsync.service.impl;

import com.amnex.rorsync.dto.RorSyncUniqueDto;
import com.amnex.rorsync.dto.request.RorSyncDto;
import com.amnex.rorsync.dto.response.ResponseModel;
import com.amnex.rorsync.dto.response.RorSyncResDto;
import com.amnex.rorsync.dto.response.RorSyncResponseDto;
import com.amnex.rorsync.entity.RorSyncData;
import com.amnex.rorsync.mapper.RorSyncMapper;
import com.amnex.rorsync.repository.RorSyncDataRepository;
import com.amnex.rorsync.service.RorSyncService;
import com.amnex.rorsync.util.CommonUtils;
import com.amnex.rorsync.util.Constants;
import com.amnex.rorsync.util.JdbcUtil;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.servlet.http.HttpServletRequest;
import java.sql.Timestamp;
import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.function.BiFunction;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.stream.Collectors;

@Service
@Transactional
public class RorSyncServiceImpl implements RorSyncService {

    @Autowired
    private RorSyncMapper rorSyncMapper;

    @Autowired
    private CommonUtils commonUtils;

    @Autowired
    private RorSyncDataRepository rorSyncDataRepository;

    @Autowired
    private JdbcUtil jdbcUtil;

    BiFunction<List<RorSyncUniqueDto>, RorSyncDto, Boolean> rorSyncDataIsExists = (list, syncData) -> list.stream().anyMatch(u -> u.getFarmerId().equals(syncData.getFarmerId()) && u.getFarmId().equals(syncData.getFarmId()));

    @Override
    public ResponseModel pushRorData(List<RorSyncDto> rorSyncDtoList, HttpServletRequest request) {
        ResponseModel model = new ResponseModel();
        try {
            if(rorSyncDtoList.isEmpty()) {
                model.setMessage(Constants.No_DATA_FOUND);
                model.setCode(Constants.NOT_FOUND_CODE);
            } else {
                String userId = commonUtils.getUserId(request);
                List<RorSyncResDto> responseDtos = new ArrayList<>();
                List<RorSyncDto> duplicateRecords = new ArrayList<>();
                final AtomicInteger counter = new AtomicInteger();
                Collection<List<RorSyncDto>> result = rorSyncDtoList.stream().collect(Collectors.groupingBy(it -> counter.getAndIncrement() / 100)).values();
                result.parallelStream().forEach(syncData -> {
                    // check duplication records
                    String combiningRecords = syncData.stream()
                        .map(rorSyncMapper::toUniqueDto)
                        .map(entry -> String.format("('%s','%s')", entry.getFarmerId(), entry.getFarmId()))
                        .collect(Collectors.joining(", "));
                    if(StringUtils.isNotEmpty(combiningRecords)) {
                        String combineRecords = jdbcUtil.findExistingRolesUsingJdbc(combiningRecords);
                        List<RorSyncUniqueDto> duplicateList = commonUtils.convertJsonToList(combineRecords, RorSyncUniqueDto.class);
                        if(!duplicateList.isEmpty()) {
                            syncData = syncData.stream().filter(rec -> {
                                boolean recordsFound = rorSyncDataIsExists.apply(duplicateList,rec);
                                if(recordsFound) {
                                    duplicateRecords.add(rec);
                                }
                                return !recordsFound;
                            }).collect(Collectors.toList());
                        }
                    }
                    if(!syncData.isEmpty()) {
                        List<RorSyncData> syncFinalData = syncData.stream()
                                .map(rorSyncMapper::toEntity)
                                .map(e -> {
                                    e.setCreatedBy(userId);
                                    e.setCreatedOn(new Timestamp(new Date().getTime()));
                                    e.setModifiedBy(userId);
                                    e.setModifiedOn(new Timestamp(new Date().getTime()));
                                    return e;
                                })
                                .collect(Collectors.toList());
                        syncFinalData = rorSyncDataRepository.saveAll(syncFinalData);
                        List<RorSyncResDto> responseList = syncFinalData.stream().map(rorSyncMapper::toResposneDto).collect(Collectors.toList());
                        if(!responseList.isEmpty()) {
                            responseDtos.addAll(responseList);
                        }
                    }
                });
                RorSyncResponseDto finalResponse = new RorSyncResponseDto();
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
