package com.spm.viettel.msm.dto.response;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import java.util.List;
@Data
@Setter
@Getter
public class AssignChecklistResponseLv1 {
    private Long planId;
    private Long checkListParentId1;
    private Long planJobParentId1;
    private String checkListParent1;
    private String checkListParentCode1;
    private Long idx1;
    private List<AssignCheckListResponseLv2> searchAssignCheckListResponseLv2;

    public AssignChecklistResponseLv1(Long planId, Long checkListParentId1, Long planJobParentId1,
                                      String checkListParent1,String checkListParentCode1, Long idx1) {
        this.planId = planId;
        this.checkListParentId1 = checkListParentId1;
        this.planJobParentId1 = planJobParentId1;
        this.checkListParent1 = checkListParent1;
        this.checkListParentCode1 =checkListParentCode1;
        this.idx1 = idx1;
    }

}
