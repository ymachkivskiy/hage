package org.hage.platform.node.execution;

import org.hage.platform.HageRuntimeException;

public class ExecutionCoreException extends HageRuntimeException {
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
