package com.spm.viettel.msm.exceptions;


import com.spm.viettel.msm.factory.IResponseStatus;

public class ValidationException extends RuntimeException {
    private static final long serialVersionUID = 7477382934189144413L;

    protected IResponseStatus errorStatus;

    public ValidationException(String message) {
        super(message);
    }

    public ValidationException(IResponseStatus errorStatus) {
        super(errorStatus.getMessage());
        this.errorStatus = errorStatus;
    }

    public ValidationException(IResponseStatus errorStatus, String message) {
        super(message);
        this.errorStatus = errorStatus;
    }

    public IResponseStatus getErrorStatus() {
        return errorStatus;
    }
}
