package org.hage.util.cmd;

public class InvalidRuntimeArgumentsException extends RuntimeException {

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
