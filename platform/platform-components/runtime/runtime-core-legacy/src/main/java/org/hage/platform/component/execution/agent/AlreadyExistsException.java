package org.hage.platform.component.execution.agent;


import org.hage.platform.HageRuntimeException;
import org.hage.platform.communication.address.agent.AgentAddress;


public class AlreadyExistsException extends HageRuntimeException {

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
