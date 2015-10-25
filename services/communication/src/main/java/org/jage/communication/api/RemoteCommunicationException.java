package org.jage.communication.api;


import org.jage.exception.AgeException;

import javax.annotation.concurrent.Immutable;


@Immutable
public class RemoteCommunicationException extends AgeException {

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
