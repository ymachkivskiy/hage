package org.hage.platform.simulation.base;

import lombok.Getter;
import org.hage.platform.HageRuntimeException;

public class UnsupportedAgentTypeException extends HageRuntimeException {

    @Getter
    private final Class<? extends Agent> agentClazz;

    public UnsupportedAgentTypeException(Class<? extends Agent> agentClazz) {
        super("Unsupported agent class: " + agentClazz);
        this.agentClazz = agentClazz;
    }

}
