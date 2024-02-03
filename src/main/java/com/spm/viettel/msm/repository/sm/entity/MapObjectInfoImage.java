package com.spm.viettel.msm.repository.sm.entity;

import lombok.Data;
import javax.persistence.*;
import java.util.Date;

@Data
@Table(name = "MAP_OBJECT_INFO_IMAGE")
public class MapObjectInfoImage {
    @Id
    @GeneratedValue(
            strategy = GenerationType.SEQUENCE,
            generator = "SEQ_GEN")
    @javax.persistence.SequenceGenerator(
            name = "SEQ_GEN",
            sequenceName = "MAP_OBJECT_INFO_IMAGE_SEQ",
            allocationSize = 1
    )
    @Column(name = "ID")
    private Long id;

    @Column(name = "TYPE")
    private Integer type;

    @Column(name = "MAP_OBJECT_INFO_ID")
    private Long mapObjectInfoId;

    @Column(name = "IMAGE_PATH")
    private String path;
    @Column(name = "IMAGE_NAME")
    private String name;

    @Column(name = "USER_CREATE")
    private String userCreated;

    @Column(name = "STATUS")
    private Boolean status;

    @Column(name = "CREATE_DATE")
    @Temporal(TemporalType.TIMESTAMP)
    private Date dateCreated;

    @Column(name = "UPDATE_DATE")
    @Temporal(TemporalType.TIMESTAMP)
    private Date dateUpdated;

    @Column(name = "ORDER_BY")
    private Integer orderBy;
}
