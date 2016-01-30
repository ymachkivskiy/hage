package org.hage.platform.component.agent;


import org.hage.platform.HageException;
import org.hage.platform.communication.address.agent.AgentAddress;


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
