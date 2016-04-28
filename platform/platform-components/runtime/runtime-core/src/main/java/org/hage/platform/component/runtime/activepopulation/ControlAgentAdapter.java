package org.hage.platform.component.runtime.activepopulation;

import lombok.RequiredArgsConstructor;
import org.hage.platform.component.runtime.unit.context.AgentExecutionContextEnvironment;
import org.hage.platform.simulation.runtime.control.ControlAgent;

@RequiredArgsConstructor
public class ControlAgentAdapter {
    private final AgentExecutionContextEnvironment environment;
    private final ControlAgent controlAgent;

    public void performStep() {
        controlAgent.step(environment.contextForControlAgent());
    }

}
