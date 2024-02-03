package com.spm.viettel.msm.dto.request;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.web.multipart.MultipartFile;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ImportPlanChannelTargetRequest {
    private MultipartFile file;
    private String planDate;
    private Integer targetLevel;
    private Integer targetType;
    private String brCode;
    private String bcCode;
}
