package org.hage.platform.component.execution;

import org.hage.platform.HageException;

public class ExecutionCoreException extends HageException {
    public ExecutionCoreException(String message) {
        super(message);
    }

    public ExecutionCoreException(Throwable cause) {
        super(cause);
    }

    public ExecutionCoreException(String message, Throwable cause) {
        super(message, cause);
    }
}
