package com.spm.viettel.msm.dto.request;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class DownloadFileRequest {
    private String fileName;
    private Long shopId;
    private Long branchId;
    private Long bcId;
    private Long staffId;
    private String dateStart;
    private String dateEnd;
    private String userType;
    private Long isVisited;
}
