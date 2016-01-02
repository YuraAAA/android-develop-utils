package com.aizenberg.support.common.error;

/**
 * Created by Yuriy Aizenberg
 */
public class SupportException extends RuntimeException {

    public SupportException() {
    }

    public SupportException(String detailMessage) {
        super(detailMessage);
    }

    public SupportException(String detailMessage, Throwable throwable) {
        super(detailMessage, throwable);
    }

    public SupportException(Throwable throwable) {
        super(throwable);
    }
}
