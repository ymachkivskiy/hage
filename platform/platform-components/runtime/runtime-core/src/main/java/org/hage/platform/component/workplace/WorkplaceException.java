package org.hage.platform.component.workplace;


import org.hage.platform.HageException;

public class WorkplaceException extends HageException {

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
