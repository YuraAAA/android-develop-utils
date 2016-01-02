package com.aizenberg.support.validation;

import com.aizenberg.support.common.error.SupportException;

/**
 * Created by Yuriy Aizenberg
 */
public class ValidationException extends SupportException {


    public ValidationException() {
    }

    public ValidationException(String detailMessage) {
        super(detailMessage);
    }

    public ValidationException(String detailMessage, Throwable throwable) {
        super(detailMessage, throwable);
    }

    public ValidationException(Throwable throwable) {
        super(throwable);
    }
}
