package com.spm.viettel.msm.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class VisitPlanImportResult {
    private Integer statusImport;
    private Integer quantityImportSuccess;
    private Integer quantityImportFailure;
    private Integer quantityRowInFile;
}
