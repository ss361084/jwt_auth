package com.amnex.fr_farmer_farm_sync.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class FrFarmerFarmUniqueDto {
    private String farmerId;
    private String farmId;
    private Long pushId;
}
