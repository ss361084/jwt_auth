package com.amnex.rorsync.entity;

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
    @CreationTimestamp
    private Timestamp createdOn;

    private String createdBy;

    @UpdateTimestamp
    private Timestamp modifiedOn;

    private String modifiedBy;

    private Boolean isDeleted = false;

    private Boolean isActive = true;
}
