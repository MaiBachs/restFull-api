package com.spm.viettel.msm.dto.response;

import com.spm.viettel.msm.dto.StaffDto;
import lombok.Data;

import java.util.List;

@Data
public class DataSearchChannelStaff {
    private List<StaffDto> staffChannel;
    private int currentPage=0;
    private Long totalRecord;
    private int totalPage;
}
