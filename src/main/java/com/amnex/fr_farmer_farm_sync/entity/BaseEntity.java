package com.amnex.fr_farmer_farm_sync.entity;

import lombok.Data;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import javax.persistence.Column;
import javax.persistence.MappedSuperclass;
import java.io.Serializable;
import java.sql.Timestamp;

@MappedSuperclass
@Data
public class BaseEntity implements Serializable {
    private static final long serialVersionUID = 1L;
    @CreationTimestamp
    private Timestamp createdOn;

    private String createdBy;

    @UpdateTimestamp
    private Timestamp modifiedOn;

    private String modifiedBy;

    private Boolean isDeleted = false;

    private Boolean isActive = true;
}
