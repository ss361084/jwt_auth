package com.amnex.rorsync.dto.response;

import com.amnex.rorsync.dto.request.RorSyncDto;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class RorSyncResponseDto {
    private List<RorSyncResDto> pushedRecords;
    private List<RorSyncDto> duplicateRecords;
}
