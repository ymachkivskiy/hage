package org.hage.cli;


import org.hage.exception.AgeException;


public class InvalidRuntimeArgumentsException extends AgeException {

    private static final long serialVersionUID = 2L;


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
