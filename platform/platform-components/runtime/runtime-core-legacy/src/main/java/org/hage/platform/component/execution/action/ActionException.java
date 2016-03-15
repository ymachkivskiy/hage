package org.hage.platform.component.execution.action;


import org.hage.platform.HageRuntimeException;

public class ActionException extends HageRuntimeException {

    public ActionException(final String message, final Throwable cause) {
        super(message, cause);
    }


    public ActionException(final String message) {
        super(message);
    }

    public ActionException(final Throwable cause) {
        super(cause);
    }

}
