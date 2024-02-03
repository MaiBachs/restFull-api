package com.spm.viettel.msm.dto.response;

import com.spm.viettel.msm.dto.StaffDto;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
public class StaffResponse {
    private List<StaffDto> staffs = new ArrayList<>();
}
