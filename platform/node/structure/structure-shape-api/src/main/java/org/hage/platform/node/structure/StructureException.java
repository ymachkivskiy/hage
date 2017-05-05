package org.hage.platform.node.structure;

import org.hage.platform.HageRuntimeException;

public class StructureException extends HageRuntimeException {

    public StructureException(String message) {
        super(message);
    }

    public StructureException(Throwable cause) {
        super(cause);
    }

    public StructureException(String message, Throwable cause) {
        super(message, cause);
    }

}
