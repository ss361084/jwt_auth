package com.amnex.fr_farmer_farm_sync.repository;

import com.amnex.fr_farmer_farm_sync.entity.UserMaster;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserMasterRepository extends JpaRepository<UserMaster,Long> {
    @Query(value = "SELECT U FROM UserMaster U WHERE U.userName=:userName AND U.isActive=true AND U.isDeleted=false")
    Optional<UserMaster> findByUserNameAndIsDeletedAndIsActive(String userName);

    @Query(value = "SELECT U FROM UserMaster U WHERE U.userId=:userId AND U.isActive=:isActive AND U.isDeleted=:isDeleted")
    Optional<UserMaster> findByUserIdAndIsDeletedAndIsActive(Long userId, Boolean isDeleted, Boolean isActive);
}
