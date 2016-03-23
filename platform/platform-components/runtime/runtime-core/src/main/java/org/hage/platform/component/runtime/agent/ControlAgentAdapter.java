package org.hage.platform.component.runtime.agent;

import lombok.RequiredArgsConstructor;
import org.hage.platform.simulation.runtime.ControlAgent;

import java.io.Serializable;

@RequiredArgsConstructor
public class ControlAgentAdapter implements Serializable {
    private final ControlAgent controlAgent;

    public void performStep() {
        // TODO: pass context to control agent
        controlAgent.step(null);
    }

}
