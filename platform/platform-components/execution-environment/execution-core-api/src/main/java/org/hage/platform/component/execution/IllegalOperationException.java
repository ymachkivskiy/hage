package org.hage.platform.component.execution;

public class IllegalOperationException extends WorkplaceException {

    public IllegalOperationException(final String message) {
        super(message);
    }

    public IllegalOperationException(final Throwable cause) {
        super(cause);
    }

    public IllegalOperationException(final String message, final Throwable cause) {
        super(message, cause);
    }
}
