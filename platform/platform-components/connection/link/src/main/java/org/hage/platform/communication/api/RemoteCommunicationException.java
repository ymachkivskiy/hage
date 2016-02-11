package org.hage.platform.communication.api;



import org.hage.platform.HageRuntimeException;

import javax.annotation.concurrent.Immutable;


@Immutable
public class RemoteCommunicationException extends HageRuntimeException {

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
