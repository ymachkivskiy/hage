package org.hage.platform;

public class HageException extends Exception {
    public HageException(String message) {
        super(message);
    }

    public HageException(String message, Throwable cause) {
        super(message, cause);
    }

    public HageException(Throwable cause) {
        super(cause);
    }
}
