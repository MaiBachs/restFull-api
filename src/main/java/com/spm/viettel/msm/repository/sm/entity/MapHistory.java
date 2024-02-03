package com.spm.viettel.msm.repository.sm.entity;

import lombok.Data;
import javax.persistence.*;
import java.util.Date;

@Data
@Entity
@Table(name = "map_history")

public class MapHistory implements java.io.Serializable {

    @Id
    @GeneratedValue(
            strategy = GenerationType.SEQUENCE,
            generator = "MAP_HISTORY_SEQ_GEN")
    @javax.persistence.SequenceGenerator(
            name = "MAP_HISTORY_SEQ_GEN",
            sequenceName = "MAP_HISTORY_SEQ",
            allocationSize = 1
    )

    @Column (name = "ID")
    private Long id;

    @Column (name = "TABLE_NAME")
    private String tableName;

    @Column(name = "DIFF")
    private String diff;

    @Column(name = "CREATED_DATE")
    private Date createdDate;

    @Column(name = "CREATED_USER")
    private String createdUser;

    @Column (name = "OBJECT_ID")
    private Long objectId;

}
