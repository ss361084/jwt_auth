package com.amnex.fr_farmer_farm_sync.repository;

import com.amnex.fr_farmer_farm_sync.entity.FrFarmerFarmData;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface FrFarmerFarmDataRepository extends JpaRepository<FrFarmerFarmData,Long> {
}
