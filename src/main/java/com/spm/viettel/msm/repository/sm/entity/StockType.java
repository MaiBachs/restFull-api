package com.spm.viettel.msm.repository.sm.entity;

import lombok.Data;

import javax.persistence.*;

@Data
@Entity
@Table(name = "STOCK_TYPE")
public class StockType {
    @Id
    @Column(name = "STOCK_TYPE_ID")
    private Long stockTypeId;

    @Column(name = "NAME")
    private String name;

    @Column(name = "STATUS")
    private Long status;

    @Column(name = "NOTES")
    private String notes;

    @Column(name = "TABLE_NAME")
    private String tableName;

    @Column(name = "CHECK_EXP")
    private Long checkExp;

    @Column(name = "BANK_ACCOUNT_GROUP_ID")
    private Long bankAccountGroupId;

    @Column(name = "INVENTORY_TYPE")
    private Long inventoryType;

}
