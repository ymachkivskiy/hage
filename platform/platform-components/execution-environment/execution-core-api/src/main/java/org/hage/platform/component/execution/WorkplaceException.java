package org.hage.platform.component.execution;

import org.hage.platform.HageRuntimeException;

public class WorkplaceException extends HageRuntimeException {

    public WorkplaceException(final String message) {
        super(message);
    }

    public WorkplaceException(final Throwable cause) {
        super(cause);
    }

    public WorkplaceException(final String message, final Throwable cause) {
        super(message, cause);
    }

}
