package com.spm.viettel.msm.repository.sm.entity;

import lombok.Data;
import javax.persistence.*;
import java.time.LocalDate;

@Data
@Entity
@Table(name = "STOCK_HANDSET")
public class StockHandset {
    @Id
    @Column(name = "ID")
    private Long id;

    @Column(name = "STOCK_MODEL_ID")
    private Long stockModelId;

    @Column(name = "NAME")
    private String name;

    @Column(name = "SERIAL")
    private String serial;

    @Column(name = "IMEI")
    private String imei;

    @Column(name = "HANDSET_TYPE")
    private String handsetType;

    @Column(name = "OWNER_ID")
    private Long ownerId;

    @Column(name = "OWNER_TYPE")
    private Long ownerType;

    @Column(name = "CREATE_DATE")
    private LocalDate createDate;

    @Column(name = "STATUS")
    private Long status;

    @Column(name = "TELECOM_SERVICE_ID")
    private Long telecomServiceId;

    @Column(name = "STATE_ID")
    private Long stateId;

    @Column(name = "CHECK_DIAL")
    private Long checkDial;

    @Column(name = "DIAL_STATUS")
    private Long dialStatus;

    @Column(name = "CREATE_USER")
    private String createUser;

    @Column(name = "USER_SESSION_ID")
    private String userSessionId;

    @Column(name = "PARTNER_ID")
    private Long partnerId;

    @Column(name = "CHANNEL_TYPE_ID")
    private Long channelTypeId;

    @Column(name = "OWNER_RECEIVER_ID")
    private Long ownerReceiverId;

    @Column(name = "OWNER_RECEIVER_TYPE")
    private Long ownerReceiverType;

    @Column(name = "RECEIVER_NAME")
    private String receiverName;

    @Column(name = "CREATE_USER_ID")
    private Long createUserId;

    @Column(name = "LAST_UPDATE_DATE")
    private LocalDate lastUpdateDate;

    @Column(name = "LAST_UPDATE_USER")
    private String lastUpdateUser;

    @Column(name = "LAST_UPDATE_USER_ID")
    private Long lastUpdateUserId;

    @Column(name = "SALE_DATE")
    private LocalDate saleDate;

    @Column(name = "DEPOSIT_PRICE")
    private Long depositPrice;

    @Column(name = "LAST_STATUS")
    private Long lastStatus;

    @Column(name = "LAST_OWNER_ID")
    private Long lastOwnerId;

    @Column(name = "LAST_OWNER_TYPE")
    private Long lastOwnerType;

    @Column(name = "LAST_STATUS_DATE")
    private LocalDate lastStatusDate;

    @Column(name = "CONTRACT_CODE")
    private String contractCode;

    @Column(name = "BATCH_CODE")
    private String batchCode;

    @Column(name = "UNLOCK_CODE")
    private String unlockCode;

    @Column(name = "FIRST_BUY_OWNER_ID")
    private Long firstBuyOwnerId;

    @Column(name = "FIRST_BUY_OWNER_TYPE")
    private Long firstBuyOwnerType;

    @Column(name = "CHANGE_DATE")
    private LocalDate changeDate;

    @Column(name = "DAY_INSTOCK")
    private Long dayInstock;

    @Column(name = "IMPORTED_DATE")
    private LocalDate importedDate;

    @Column(name = "PROPERTIES")
    private Long properties;

    @Column(name = "RETAIL_PRICE_DEALER")
    private Long retailPriceDealer;

    @Column(name = "LAST_IMPORT_STOCK_DATE")
    private LocalDate lastImportStockDate;


}
