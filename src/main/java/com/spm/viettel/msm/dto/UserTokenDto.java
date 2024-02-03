package com.spm.viettel.msm.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.spm.viettel.msm.repository.sm.entity.AppParamsSM;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;
import java.util.ArrayList;
import java.util.List;
import static com.fasterxml.jackson.annotation.JsonInclude.Include.NON_NULL;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@ToString
@JsonInclude(NON_NULL)
public class UserTokenDto {
    private String shopType;
    private String posCode;
    private String staffCode;
    private Long userID;
    private String loginName = null;
    private String fullName = null;
    private Long groupID;
    private Long groupLevel;
    private String assigneeID;
    private boolean belongToManyGroup = false;
    private Long shopId;
    private Long channelTypeId;
    private String shopCode;
    private String shopName;
    private String staffName;
    private int numberProfileOutDate;
    private List<String> roles = new ArrayList<>();
    private List<AppParamsDTO> userTypes;
    private Boolean visitImport;
    private ShopTreeDTO branch;
    private ShopTreeDTO bc;
}
