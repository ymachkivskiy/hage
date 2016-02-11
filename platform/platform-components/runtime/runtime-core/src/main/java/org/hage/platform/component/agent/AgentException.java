package org.hage.platform.component.agent;


import org.hage.platform.HageRuntimeException;

public class AgentException extends HageRuntimeException {

    public AgentException(final String message) {
        super(message);
    }


    public AgentException(final Throwable cause) {
        super(cause);
    }


    public AgentException(final String message, final Throwable cause) {
        super(message, cause);
    }

}
