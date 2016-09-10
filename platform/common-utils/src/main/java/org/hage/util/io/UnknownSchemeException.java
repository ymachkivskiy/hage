package org.hage.util.io;


public class UnknownSchemeException extends Exception {

    public UnknownSchemeException() {
        super();
    }


    public UnknownSchemeException(String message) {
        super(message);
    }


    public UnknownSchemeException(Throwable cause) {
        super(cause);
    }


    public UnknownSchemeException(String message, Throwable cause) {
        super(message, cause);
    }

}
