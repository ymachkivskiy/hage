package org.hage.platform.component.simulation.structure;

import org.hage.platform.component.simulation.structure.definition.Position;

//// TODO: 17.02.16 add cell identifier (new address)
public interface SimulationCell {
    Position getPosition();

    void performAgentsStep();

    void performControlAgentStep();
}
