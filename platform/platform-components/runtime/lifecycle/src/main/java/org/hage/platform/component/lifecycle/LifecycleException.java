package org.hage.platform.component.lifecycle;


import org.hage.platform.HageException;

import javax.annotation.concurrent.Immutable;


@Immutable
public class LifecycleException extends HageException {

    private static final long serialVersionUID = 1L;


    public LifecycleException(final String message) {
        super(message);
    }


    public LifecycleException(final String message, final Throwable cause) {
        super(message, cause);
    }


    public LifecycleException(final Throwable cause) {
        super(cause);
    }
}
