package com.spm.viettel.msm.dto.enums;

import com.fasterxml.jackson.annotation.JsonEnumDefaultValue;

public enum ActionChannelType {
    @JsonEnumDefaultValue
    LIST,
    ADD, EDIT, VIEW, DELETE, APPROVE, REJECT, CANCEL,CHANNEL_TYPE_CAN_PLAN_DEVELOP,ALL,
    LIST_CHANNEL_BY_IMAGE, LIST_RESULT, LIST_MAP_PLAN, CHECK_LIST_RESULT_OF_CHANNEL,
    SET_STATUS, SEARCH, SET_STATUS_PLAN, POINTS_NEAR_CHANNEL, GET_FILE, LIST_BTS, GET_PROVINCE, GET_DISTRICT, GET_PRECINCT

}
