package com.spm.viettel.msm.repository.sm.entity;

import lombok.Data;

import javax.persistence.*;

@Data
@Entity
@Table(name = "OTHER_OBJECT_TYPE")
public class OtherObjectType implements java.io.Serializable {

    //Fields
    @Id
    @Column(name = "OTHER_CHANNEL_TYPE_ID")
    private Long id;
    @Column(name = "NAME")
    private String name;
    @Column(name = "object_type")
    private String objectType;
    @Column(name = "channel_type_id")
    private Long channelTypeId;
    @Column(name = "STATUS")
    private Integer status;
    @Column(name = "is_vt_unit")
    private String isVtUnit;
    @Column(name = "create_date")
    private String createDate;
    @Column(name = "update_date")
    private String updateDate;
    @Column(name = "prefix_object_code")
    private String prefixObjectCode;
    @Column(name = "show_on_map")
    private Integer showOnMap;
    @Column(name = "mkt")
    private Integer mkt;
}
