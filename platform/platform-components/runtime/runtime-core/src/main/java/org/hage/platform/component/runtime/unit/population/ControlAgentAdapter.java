package org.hage.platform.component.runtime.unit.population;

import lombok.RequiredArgsConstructor;
import org.hage.platform.component.runtime.unit.agentcontext.AgentLocalEnvironment;
import org.hage.platform.simulation.runtime.control.ControlAgent;

@RequiredArgsConstructor
public class ControlAgentAdapter {
    private final AgentLocalEnvironment environment;
    private final ControlAgent controlAgent;

    public void performStep() {
        controlAgent.step(environment.contextForControlAgent());
    }

}
