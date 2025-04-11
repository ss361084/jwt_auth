package com.amnex.fr_farmer_farm_sync.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.io.Serializable;

@Data
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode(callSuper = false)
@Entity
@Table(name = "fr_farmer_farm_data",schema = "public",
        uniqueConstraints = @UniqueConstraint(columnNames = {"farmer_id", "farm_id"})
)
public class FrFarmerFarmData extends BaseEntity implements Serializable {
    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "vault_ref_number")
    private String vaultRefNumber;

    @Column(name = "farmer_status")
    private String farmerStatus;

    @Column(name = "farmer_id")
    private String farmerId;

    @Column(name = "push_id")
    private Long pushId;

    @Column(name = "owner_name")
    private String ownerName;

    @Column(name = "identifier_name")
    private String identifierName;

    @Column(name = "survey_no")
    private String surveyNo;

    @Column(name = "farm_id")
    private String farmId;

    @Column(name = "village_lgd_code")
    private Long villageLgdCode;

    @Column(name = "unique_code")
    private String uniqueCode;

    @Column(name = "phone_number")
    private String phoneNumber;
}
