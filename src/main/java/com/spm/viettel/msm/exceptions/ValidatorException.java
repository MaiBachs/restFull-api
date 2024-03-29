package com.spm.viettel.msm.exceptions;

public class ValidatorException extends RuntimeException {
    private final String fieldName;

    public ValidatorException(final String message, final String fieldName) {
        super(message);
        this.fieldName = fieldName;
    }

    public ValidatorException(final String message, final Throwable cause, final String fieldName) {
        super(message, cause);
        this.fieldName = fieldName;
    }

    public ValidatorException(final Throwable cause, final String fieldName) {
        super(cause);
        this.fieldName = fieldName;
    }

    public String getFieldName() {
        return this.fieldName;
    }

    public synchronized Throwable fillInStackTrace() {
        return this;
    }
}
