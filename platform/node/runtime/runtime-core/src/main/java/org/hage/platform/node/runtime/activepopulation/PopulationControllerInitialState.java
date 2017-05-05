package org.hage.platform.node.runtime.activepopulation;

import lombok.Getter;
import org.hage.platform.simulation.runtime.agent.Agent;
import org.hage.platform.simulation.runtime.control.ControlAgent;

import java.util.List;
import java.util.Optional;

import static java.util.Optional.ofNullable;
import static org.hage.util.CollectionUtils.nullSafe;

@Getter
public class PopulationControllerInitialState {
    private final Optional<ControlAgent> controlAgent;
    private final List<Agent> agents;

    private PopulationControllerInitialState(Optional<ControlAgent> controlAgent, List<Agent> agents) {
        this.controlAgent = controlAgent;
        this.agents = agents;
    }

    public static PopulationControllerInitialState initialStateWithControlAgentAndAgents(ControlAgent controlAgent, List<Agent> agents) {
        return new PopulationControllerInitialState(ofNullable(controlAgent), nullSafe(agents));
    }

    public static PopulationControllerInitialState initialStateWithControlAgent(ControlAgent controlAgent) {
        return initialStateWithControlAgentAndAgents(controlAgent, null);
    }


}
