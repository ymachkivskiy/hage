package org.hage.platform;


public class HageRuntimeException extends RuntimeException {

    public HageRuntimeException(final String message) {
        super(message);
    }

    public HageRuntimeException(final Throwable cause) {
        super(cause);
    }

    public HageRuntimeException(final String message, final Throwable cause) {
        super(message, cause);
    }
}
