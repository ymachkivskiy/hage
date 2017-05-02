package org.hage.platform.config;

import org.hage.platform.HageRuntimeException;

public class InvalidRuntimeArgumentsException extends HageRuntimeException {

    public InvalidRuntimeArgumentsException(final String message) {
        super(message);
    }

    public InvalidRuntimeArgumentsException(final Throwable cause) {
        super(cause);
    }

    public InvalidRuntimeArgumentsException(final String message, final Throwable cause) {
        super(message, cause);
    }
}
