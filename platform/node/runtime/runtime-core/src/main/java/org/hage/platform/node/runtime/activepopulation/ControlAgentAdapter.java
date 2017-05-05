package org.hage.platform.node.runtime.activepopulation;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.hage.platform.node.runtime.unit.AgentExecutionContextEnvironment;
import org.hage.platform.simulation.runtime.control.ControlAgent;

@RequiredArgsConstructor
public class ControlAgentAdapter {
    private final AgentExecutionContextEnvironment environment;
    @Getter
    private final ControlAgent controlAgent;

    public void performStep() {
        controlAgent.step(environment.contextForControlAgent());
    }

}
