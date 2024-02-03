package com.spm.viettel.msm.repository.sm.entity;

import lombok.Data;
import oracle.sql.CLOB;
import javax.persistence.*;
import java.util.Date;

@Data
@Entity
@Table(name = "sms", schema = "cm_pos")
public class Sms implements java.io.Serializable{
    @Id
    @GeneratedValue(
            strategy = GenerationType.SEQUENCE,
            generator = "CMPOS_SMS_SEQ_GEN")
    @SequenceGenerator(
            name = "CMPOS_SMS_SEQ_GEN",
            sequenceName = "CM_POS.SMS_SEQ",
            allocationSize = 1
    )
    @Column(name = "ID")
    private Long id;
    @Column(name = "ISDN")
    private String isDn;
    @Column(name = "CONTENT_BK")
    private CLOB contentBK;
    @Column(name = "SEND_TIME")
    private Date sendTime;
    @Column(name = "NUM_PROCESS")
    private Integer numProcess;
    @Column(name = "MAX_PROCESS")
    private Integer maxProcess;
    @Column(name = "LOG")
    private CLOB log;
    @Column(name = "STATUS")
    private Long status;
    @Column(name = "CREATE_USER")
    private String createUser;
    @Column(name = "CREATE_TIME")
    private Date createTime;
    @Column(name = "UPDATE_USER")
    private String updateUser;
    @Column(name = "UPDATE_TIME")
    private Date updateTime;
    @Column(name = "APP")
    private String app;
    @Column(name = "CONTENT")
    private String content;
    @Column(name = "TYPE_SMS")
    private String typeSMS;
    @Column(name = "IS_INTERNAL")
    private String isInternal;
    @Column(name = "SMS_CODE")
    private String SMSCode;
    @Column(name = "SEND_BY")
    private String sendBy;
    @Column(name = "SEND_SMS_CODE")
    private String sendSMSCode;
    @Column(name = "SEND_TYPE_SMS")
    private String sendTypeSMS;
}
