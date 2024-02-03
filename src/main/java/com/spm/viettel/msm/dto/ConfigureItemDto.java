package com.spm.viettel.msm.dto;

import com.spm.viettel.msm.repository.smartphone.entity.Reason;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.util.Date;
import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ConfigureItemDto {
    private Long evaluationId;
    private String evaluationName;
    private Long groupId;
    private String groupName;
    private Long jobId;
    private String jobName;
    private Long id;
    private String channelType;
    private Long channelTypeId;
    private Float percent;
    private float percentf = 0;
    private Long ok;
    private Long nok;
    private Long na;
    private String validation;
    private String url;
    private Long status;
    private Date createdDate;
    private Date lastUpdate;
    private List<Reason> reasonList;
    private FileDto fileDto = new FileDto();
    private String fileName;
    private Boolean successUpload;

    private Long photo = 0l;
    private Long video = 0l;
    private Long audio = 0l;
    private Long file = 0l;
    private String reasonNok;
    private String gravedad;

    public ConfigureItemDto(Long id, Long channelTypeId, Long evaluationId, Long groupId, Long jobId, Float percent, Long ok, Long nok, Long na, String validation, String url, Long status, Date createdDate, Date lastUpdate, String fileName) {
        this.id = id;
        this.channelTypeId = channelTypeId;
        this.percent = percent;
        this.ok = ok;
        this.nok = nok;
        this.na = na;
        this.validation = validation;
        this.url = url;
        this.status = status;
        this.createdDate = createdDate;
        this.lastUpdate = lastUpdate;
        this.evaluationId = evaluationId;
        this.groupId = groupId;
        this.jobId = jobId;
        this.fileName = fileName;
    }
}
