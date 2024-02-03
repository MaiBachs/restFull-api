/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.spm.viettel.msm.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

import static com.fasterxml.jackson.annotation.JsonInclude.Include.NON_NULL;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@JsonInclude(NON_NULL)

/**
 *
 * @author luan
 */
public class AreaDto implements Serializable {
    private String type;
    private String areaCode;
    private String parentCode;
    private String cenCode;
    private String province;
    private String district;
    private String precinct;
    private String streetBlock;
    private String street;
    private String name;
    private String fullName;
    private Double orderNo;
    private String pstnCode;
    private Double status;
    //    private Date createdTime;
    private Double createdUserId;
    private String createdUser;
    //    private Date lastUpdatedTime;
    private Double lastUpdatedUserId;
    private String lastUpdatedUser;
    private String mapStatus;
    private Double eachReportTime;
    private String areaMapVtmap;

}
