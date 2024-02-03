package com.spm.viettel.msm.dto.request;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.*;

import static com.fasterxml.jackson.annotation.JsonInclude.Include.NON_NULL;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@ToString
@JsonInclude(NON_NULL)
public class StaffBaseRequest extends BaseRequestDTO {
    private Long shopId;
    private String code;
    private String name;
    private String shopCode;
}
