package com.amnex.rorsync.repository;

import com.amnex.rorsync.entity.RorSyncData;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RorSyncDataRepository extends JpaRepository<RorSyncData,Long> {
}
