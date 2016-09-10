package org.hage.platform.simulation.runtime.context;

import lombok.Getter;
import org.hage.platform.HageRuntimeException;
import org.hage.platform.simulation.runtime.agent.Agent;

public class UnsupportedAgentTypeException extends HageRuntimeException {

    @Getter
    private final Class<? extends Agent> agentClazz;

    public UnsupportedAgentTypeException(Class<? extends Agent> agentClazz) {
        super("Unsupported agent class: " + agentClazz);
        this.agentClazz = agentClazz;
    }

}
