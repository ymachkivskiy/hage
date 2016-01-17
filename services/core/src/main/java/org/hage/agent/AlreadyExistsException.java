package org.hage.agent;


import org.hage.address.agent.AgentAddress;
import org.hage.platform.HageException;


public class AlreadyExistsException extends HageException {

    public AlreadyExistsException(final AgentAddress address) {
        super("Address already exists [address: " + address.toString() + "]");
    }


    public AlreadyExistsException(final String message) {
        super(message);
    }


    public AlreadyExistsException(final Throwable cause) {
        super(cause);
    }

    public AlreadyExistsException(final String message, final Throwable cause) {
        super(message, cause);
    }
}
