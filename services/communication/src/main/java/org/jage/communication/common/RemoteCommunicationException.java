package org.jage.communication.common;


import org.jage.exception.AgeException;

import javax.annotation.concurrent.Immutable;


@Immutable
public class RemoteCommunicationException extends AgeException {

    private static final long serialVersionUID = -2313554547438876828L;

    public RemoteCommunicationException(final String message, final Throwable cause) {
        super(message, cause);
    }

    public RemoteCommunicationException(final String message) {
        super(message);
    }

    public RemoteCommunicationException(final Throwable cause) {
        super(cause);
    }

}
