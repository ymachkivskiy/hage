package org.hage.property;


import org.hage.platform.HageException;

public class PropertyException extends HageException {

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
