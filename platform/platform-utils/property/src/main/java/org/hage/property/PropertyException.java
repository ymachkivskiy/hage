package org.hage.property;


import org.hage.platform.HageRuntimeException;

public class PropertyException extends HageRuntimeException {

    private static final long serialVersionUID = 1L;


    public PropertyException(final String msg) {
        super(msg);
    }


    public PropertyException(final Throwable e) {
        super(e);
    }


    public PropertyException(final String msg, final Throwable e) {
        super(msg, e);
    }
}
