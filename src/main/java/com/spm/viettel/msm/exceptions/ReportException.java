package com.spm.viettel.msm.exceptions;

public class ReportException extends ValidatorException {
    public ReportException(final Throwable cause, final String fieldName) {
        super("report", cause, fieldName);
    }
}
