package com.spm.viettel.msm.dto;

import com.spm.viettel.msm.utils.DateUtil;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.time.DateFormatUtils;

import javax.persistence.ConstructorResult;
import javax.persistence.SqlResultSetMapping;
import java.util.Date;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class StaffDto extends BaseObject{
    private Long id;
    private String code;
    private String name;
    private Long shopId;
    private Long staffOwnerId;
    private String channelObjectType; // 1: Shop; 2: Staff
    private Long channelTypeId;
    private Double longitude;
    private Double latitude;
    private Float radius;
    private Integer status;
    private String channelTypeName;
    private String channelName;
    private String objectType;
    private String note;
    private String idIssuePlace;
    private Date idIssueDate;
    private Long type;
    private String serial;
    private Double x;
    private Double y;
    private String tel;
    private String isdn;
    private String pin;
    private String province;
    private String staffOwnerCode;
    private String staffOwnType;
    private String staffOwnerName;
    private String staffOwnerTel;
    private String pricePolicy;
    private String discountPolicy;
    private String pointOfSale;
    private Long lockStatus;
    private Date lastLockTime;
    private String mapStatus;
    private Long staffLevel;
    private Long staffType;
    private String isdnAgent;
    private Long trackingStatus;
    private String idNo;
    private String mapIcon;
    private String posCode;
    private Integer allowVisitPlan;
    private Integer totalAcumulate;
    private String separator = " - ";
    private String rank;
    private String planedDate;
    private String checkedDate;
    private boolean isPlan = false;
    private boolean isCheckedIn = false;
    private Long numRegister;
    private String createDate;
    private String shopCode;
    private String address;
    private List<SaleBtsSummaryDto> saleBtsSummaries;

//    Constructor mapping sqlSearchMap
    public StaffDto(Long id, String code, String name,Long channelTypeId,String address, Double x, Double y,String tel, String channelObjectType,String rank,Long staffOwnerId,String staffOwnerName, String staffOwnerCode,String staffOwnerTel, String isdnAgent, Long type,String objectType, String shopCode, Integer allowVisitPlan, Integer totalAcumulate  ) {
        this.id = id;
        this.code = code;
        this.name = name;
        this.staffOwnerId = staffOwnerId;
        this.channelObjectType = channelObjectType;
        this.channelTypeId = channelTypeId;
        this.objectType = objectType;
        this.type = type;
        this.x = x;
        this.y = y;
        this.tel = tel;
        this.staffOwnerCode = staffOwnerCode;
        this.staffOwnerName = staffOwnerName;
        this.staffOwnerTel = staffOwnerTel;
        this.isdnAgent = isdnAgent;
        this.allowVisitPlan = allowVisitPlan;
        this.totalAcumulate = totalAcumulate;
        this.rank = rank;
        this.shopCode = shopCode;
        this.address = address;
    }

    public StaffDto(Long id, Long shopId, String code, String name, Long staffOwnerId, Long channelTypeId, Double longitude, Double latitude, Float radius) {
        this.id = id;
        this.code = code;
        this.name = name;
        this.shopId = shopId;
        this.staffOwnerId = staffOwnerId;
        this.channelTypeId = channelTypeId;
        this.longitude = longitude;
        this.latitude = latitude;
        this.radius = radius;
    }

    public StaffDto(Long id,Long shopId, String code,  String name, Long staffOwnerId,String staffOwnerCode, Long channelTypeId, Double longitude, Double latitude, Float radius) {
        this.id = id;
        this.code = code;
        this.name = name;
        this.shopId = shopId;
        this.staffOwnerId = staffOwnerId;
        this.staffOwnerCode = staffOwnerCode;
        this.channelTypeId = channelTypeId;
        this.longitude = longitude;
        this.latitude = latitude;
        this.radius = radius;
    }

    public StaffDto(String code, Double longitude, Double latitude, String objectType, String channelObjectType) {
        this.code = code;
        this.channelObjectType = channelObjectType;
        this.longitude = longitude;
        this.latitude = latitude;
        this.objectType = objectType;
    }


    public String getMapIcon() {
        if(mapIcon == null)
            mapIcon = getObjectType() + "-" + getChannelTypeId();
        return mapIcon;
    }

    public void setCheckedDate(String checkedDate) {
        this.checkedDate = checkedDate;

        if (StringUtils.isNotEmpty(checkedDate)) {
            String currentDate = DateFormatUtils.format(new Date(), "dd/MM/yyyy");
            Long nearCheckedIn = -1000L;
            for (String d : checkedDate.split(",")) {
                Long diff = DateUtil.diffBetween2DatesByDay(currentDate, d.trim(), "dd/MM/yyyy");
                if (diff > nearCheckedIn && diff <= 0) {
                    nearCheckedIn = diff;
                }
                if (diff > 0) {
                    break;
                }
            }
            Long nearPlaned = -1000L;
            if (StringUtils.isNotEmpty(this.planedDate)) {
                for (String d : this.planedDate.split(",")) {
                    Long diff = DateUtil.diffBetween2DatesByDay(currentDate, d.trim(), "dd/MM/yyyy");
                    if (diff > nearPlaned && diff <= 0) {
                        nearPlaned = diff;
                    }
                    if (diff > 0) {
                        break;
                    }
                }
            }
            if (nearCheckedIn.longValue() == nearPlaned.longValue()){
                this.isCheckedIn= true;
            }
        }
    }
}
