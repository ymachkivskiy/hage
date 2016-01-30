package org.hage.platform.component.exception;


import org.hage.platform.HageException;

public class ComponentException extends HageException {

    private static final long serialVersionUID = 1L;

    public ComponentException(final String message) {
        super(message);
    }


    public ComponentException(final Throwable cause) {
        super(cause);
    }


    public ComponentException(final String message, final Throwable cause) {
        super(message, cause);
    }
}
