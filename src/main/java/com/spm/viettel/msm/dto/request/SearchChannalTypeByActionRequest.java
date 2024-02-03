package com.spm.viettel.msm.dto.request;


import com.spm.viettel.msm.dto.enums.ActionChannelType;

public class SearchChannalTypeByActionRequest {
    private ActionChannelType action;

    public ActionChannelType getAction() {
        return action;
    }

    public void setAction(ActionChannelType action) {
        this.action = action;
    }
}
