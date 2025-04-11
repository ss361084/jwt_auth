package com.amnex.rorsync.service.impl;

import com.amnex.rorsync.dto.request.RorSyncDto;
import com.amnex.rorsync.dto.response.ResponseModel;
import com.amnex.rorsync.dto.response.RorSyncResDto;
import com.amnex.rorsync.entity.RorSyncData;
import com.amnex.rorsync.mapper.RorSyncMapper;
import com.amnex.rorsync.repository.RorSyncDataRepository;
import com.amnex.rorsync.service.RorSyncService;
import com.amnex.rorsync.util.CommonUtils;
import com.amnex.rorsync.util.Constants;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

@Service
public class RorSyncServiceImpl implements RorSyncService {

    @Autowired
    private RorSyncMapper rorSyncMapper;

    @Autowired
    private CommonUtils commonUtils;

    @Autowired
    private RorSyncDataRepository rorSyncDataRepository;

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
                final AtomicInteger counter = new AtomicInteger();
                Collection<List<RorSyncDto>> result = rorSyncDtoList.stream().collect(Collectors.groupingBy(it -> counter.getAndIncrement() / 100)).values();
                result.parallelStream().forEach(syncData -> {
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
                });
                model.setData(responseDtos);
                model.setCode(Constants.SUCCESS_CODE);
                model.setMessage(Constants.SUCCESS);
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return model;
    }
}
