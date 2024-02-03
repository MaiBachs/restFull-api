package com.spm.viettel.msm.repository.sm.entity;

import lombok.Data;
import org.apache.commons.lang3.StringUtils;

import javax.persistence.*;

@Data
@Entity
@Table(name = "MAP_PLAN_CHANNEL_CL_RESULT")
public class MapPlanChannelCheckListResult{

    //Fields
    @Id
    @Column(name = "ID")
    private Long id;
    @Column(name = "RESULT")
    private String result;
    @Column(name = "CL_COMMENT")
    private String clComment;
    @Column(name = "MAP_PLAN_CHANNEL_CHECK_LIST_ID")
    private Long mapPlanChannelCheckListId;
    @Column(name = "MAP_PLAN_CHANNEL_ID")
    private Long mapPlanChannelId;
    @Transient
    private String fileName;

    public String extractFileName() {
        if (StringUtils.isNotEmpty(getResult())) {
            String fullPath = getResult();
            int index = fullPath.lastIndexOf("/");
            String s = fullPath.substring(index + 1);
            setFileName(s);
            return s;
        }
        return null;
    }
}
