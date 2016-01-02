package com.aizenberg.support.common.async;

import com.aizenberg.support.common.error.SupportException;

/**
 * Created by Yuriy Aizenberg
 */
public class EmptyResultException extends SupportException {

    public EmptyResultException() {
    }

    public EmptyResultException(String detailMessage) {
        super(detailMessage);
    }

    public EmptyResultException(String detailMessage, Throwable throwable) {
        super(detailMessage, throwable);
    }

    public EmptyResultException(Throwable throwable) {
        super(throwable);
    }
}
