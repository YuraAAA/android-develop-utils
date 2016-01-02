package com.aizenberg.support.logger;

import com.aizenberg.support.common.error.SupportException;

/**
 * Created by Yuriy Aizenberg
 */
public class LoggerException extends SupportException {

    public LoggerException() {
    }

    public LoggerException(String detailMessage) {
        super(detailMessage);
    }

    public LoggerException(String detailMessage, Throwable throwable) {
        super(detailMessage, throwable);
    }

    public LoggerException(Throwable throwable) {
        super(throwable);
    }
}

