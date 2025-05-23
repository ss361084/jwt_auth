package com.amnex.fr_farmer_farm_sync.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.io.Serializable;

@Entity
@Data
@Table
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode(callSuper = false)
public class UserMaster extends BaseEntity implements Serializable {
    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long userId;

    @Column(name = "user_name",columnDefinition = "VARCHAR(200)")
    private String userName;

    @Column(name = "user_password", columnDefinition = "VARCHAR(100)")
    private String userPassword;

    @Column(name = "user_token", columnDefinition = "TEXT")
    private String userToken;

    @OneToOne
    @JoinColumn(name = "role_id",referencedColumnName = "role_id")
    private RoleMaster roleMaster;
}
