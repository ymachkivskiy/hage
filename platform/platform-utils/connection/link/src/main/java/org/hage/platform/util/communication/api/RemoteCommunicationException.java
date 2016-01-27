package org.hage.platform.util.communication.api;



import org.hage.platform.HageException;

import javax.annotation.concurrent.Immutable;


@Immutable
public class RemoteCommunicationException extends HageException {

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
