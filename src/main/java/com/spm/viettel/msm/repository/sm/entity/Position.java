package com.spm.viettel.msm.repository.sm.entity;

import lombok.Data;

import javax.persistence.*;

@Data
@Entity
@Table(name = "POSITION")
public class Position {
    @Id
    @Column(name = "POS_ID")
    private Long posId;

    @Column(name = "STATUS")
    private Long status;

    @Column(name = "POS_NAME")
    private String posName;

    @Column(name = "DESCRIPTION")
    private String description;

    @Column(name = "POS_CODE")
    private String posCode;

    @Column(name = "POSITION_PER_SHOP")
    private Long positionPerShop;

    @Column(name = "POS_PREFIX")
    private String posPrefix;

    @Column(name = "SHOW_IN_CHANNEL_SHOP")
    private String showInChannelShop;
}
