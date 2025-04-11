package com.amnex.rorsync.repository;

import com.amnex.rorsync.entity.RorSyncData;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Set;

@Repository
public interface RorSyncDataRepository extends JpaRepository<RorSyncData,Long> {
}
