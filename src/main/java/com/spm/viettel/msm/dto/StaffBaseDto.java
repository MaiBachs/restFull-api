package com.spm.viettel.msm.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import static com.fasterxml.jackson.annotation.JsonInclude.Include.NON_NULL;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class StaffBaseDto {
    private Long id;
    private String code;
    private String name;
    private Long shopId;
    private String tel;
    private Long staffOwnerId;
    private String staffOwnerName;
    private String staffOwnerCode;
    private String staffOwnerTel;
    private Long channelTypeId;
    private String channelObjectType; // 1: Shop; 2: Staff
    private String address;
    private String separator = " - ";
    private Float longitude;
    private Float latitude;
    private Float radius;
    private String posCode;

    public void setSeparator(String separator) {
        this.separator = separator;
    }

    public StaffBaseDto(Long id, String code, String name, Long shopId, String tel, Long staffOwnerId, String staffOwnerName, String staffOwnerCode, String staffOwnerTel, Long channelTypeId, String channelObjectType, String address,Float longitude,Float latitude, Float radius) {
        this.id = id;
        this.code = code;
        this.name = name;
        this.shopId = shopId;
        this.tel = tel;
        this.staffOwnerId = staffOwnerId;
        this.staffOwnerName = staffOwnerName;
        this.staffOwnerCode = staffOwnerCode;
        this.staffOwnerTel = staffOwnerTel;
        this.channelTypeId = channelTypeId;
        this.channelObjectType = channelObjectType;
        this.address = address;
        this.longitude = longitude;
        this.latitude = latitude;
        this.radius = radius;
    }
}
