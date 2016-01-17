package org.hage.platform;


public class HageException extends RuntimeException {

    public HageException(final String message) {
        super(message);
    }

    public HageException(final Throwable cause) {
        super(cause);
    }

    public HageException(final String message, final Throwable cause) {
        super(message, cause);
    }
}
