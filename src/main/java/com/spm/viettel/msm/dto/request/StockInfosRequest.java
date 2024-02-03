package com.spm.viettel.msm.dto.request;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import static com.fasterxml.jackson.annotation.JsonInclude.Include.NON_NULL;
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Data
@JsonInclude(NON_NULL)
public class StockInfosRequest{
    private Long ownId;
    private String channelObj;


}
