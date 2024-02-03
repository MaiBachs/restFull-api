package com.spm.viettel.msm.dto.response;


import com.spm.viettel.msm.dto.request.PlanJobRequest;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
public class AssignCheckListResponseLv2 {
    private Long planId;
    private Long parentId;
    private Long checkListParentId2;
    private Long planJobParentId2;
    private String checkListParent2;
    private String checkListParentCode2;
    private Long idx2;
    private List<PlanJobRequest> checkListParent3s;

    public AssignCheckListResponseLv2(Long planId, Long parentId, Long checkListParentId1, Long planJobParentId1,
                                      String checkListParent1,String checkListParentCode2, Long idx2) {
        this.planId = planId;
        this.parentId = parentId;
        this.checkListParentId2 = checkListParentId1;
        this.planJobParentId2 = planJobParentId1;
        this.checkListParent2 = checkListParent1;
        this.checkListParentCode2 = checkListParentCode2;
        this.idx2 = idx2;
    }
}
