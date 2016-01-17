package org.hage.action;


import org.hage.platform.HageException;

public class ActionException extends HageException {

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
