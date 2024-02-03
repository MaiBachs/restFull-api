package com.spm.viettel.msm.dto.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class LogResponse{
    private int currentPage= 1;
    private int totalRecord;
    private Long totalPage;
    List<String> fileNames = new ArrayList<>();
}
