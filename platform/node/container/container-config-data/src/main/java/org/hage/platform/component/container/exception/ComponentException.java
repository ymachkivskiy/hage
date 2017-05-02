package org.hage.platform.component.container.exception;


import org.hage.platform.HageRuntimeException;

public class ComponentException extends HageRuntimeException {

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
