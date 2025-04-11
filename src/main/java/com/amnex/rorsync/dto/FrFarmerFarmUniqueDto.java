package com.amnex.rorsync.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class FrFarmerFarmUniqueDto {
    private String farmerId;
    private String farmId;
    private Long pushId;
}
