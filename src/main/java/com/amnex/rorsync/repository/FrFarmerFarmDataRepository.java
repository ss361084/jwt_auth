package com.amnex.rorsync.repository;

import com.amnex.rorsync.entity.FrFarmerFarmData;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface FrFarmerFarmDataRepository extends JpaRepository<FrFarmerFarmData,Long> {
}
