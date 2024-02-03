package com.spm.viettel.msm.dto.response;

import com.spm.viettel.msm.dto.ChannelWithGroupDTO;
import lombok.AccessLevel;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import java.io.File;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Data
public class PlanChannelTargetImportResponse {
    private String planDate;
    private Integer targetLevel;
    private Integer targetType;
    private File inputFile;
    private String inputFileFileName;
    private String inputFileContentType;
    private String brCode;
    private String bcCode;
    private List<ChannelWithGroupDTO> channelTypes;
    private Map<String, String> headerMapper = new HashMap<>();
    private Map<String, String> headerMapperRevert = new HashMap<>();
}
