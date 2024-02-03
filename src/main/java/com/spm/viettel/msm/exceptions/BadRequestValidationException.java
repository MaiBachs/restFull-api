package com.spm.viettel.msm.exceptions;


import com.spm.viettel.msm.dto.response.ResponseStatus;
import com.spm.viettel.msm.factory.IResponseStatus;

public class BadRequestValidationException extends ValidationException {
    private static final long serialVersionUID = 7801285748375533554L;

    public BadRequestValidationException(IResponseStatus errorStatus) {
        super(errorStatus);
    }



    public BadRequestValidationException(String code, String message) {
        super(message);
        errorStatus = new ResponseStatus(code, message);
    }

    public BadRequestValidationException(String message) {
        super(message);
    }

    public BadRequestValidationException(String code, String message, String detailMessage) {
        super(detailMessage);
        errorStatus = new ResponseStatus(code, message);
    }

    @Override
    public IResponseStatus getErrorStatus() {
        return super.getErrorStatus();
    }
}
