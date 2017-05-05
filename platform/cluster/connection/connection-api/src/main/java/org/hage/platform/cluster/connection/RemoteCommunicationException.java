package org.hage.platform.cluster.connection;

import org.hage.platform.HageException;

public class RemoteCommunicationException extends HageException {
    public RemoteCommunicationException(String message) {
        super(message);
    }

    public RemoteCommunicationException(String message, Throwable cause) {
        super(message, cause);
    }

    public RemoteCommunicationException(Throwable cause) {
        super(cause);
    }
}
