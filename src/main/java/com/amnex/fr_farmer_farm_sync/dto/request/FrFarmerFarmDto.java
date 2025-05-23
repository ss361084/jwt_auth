package com.amnex.fr_farmer_farm_sync.dto.request;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class FrFarmerFarmDto {
    private String vaultRefNumber;
    private String farmerStatus;
    private String farmerId;
    private Long pushId;
    private String ownerName;
    private String identifierName;
    private String surveyNo;
    private String farmId;
    private String villageLgdCode;
    private String uniqueCode;
    private String phoneNumber;
    private String vaultRefSource;
    private String farmerAddress;
    private String farmerEmail;
    private String farmerAadhaarSha256;
}
