package com.amnex.rorsync.service;

import com.amnex.rorsync.dto.request.RorSyncDto;
import com.amnex.rorsync.dto.response.ResponseModel;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

public interface RorSyncService {

    public ResponseModel pushRorData(List<RorSyncDto> rorSyncDtoList, HttpServletRequest request);
}
