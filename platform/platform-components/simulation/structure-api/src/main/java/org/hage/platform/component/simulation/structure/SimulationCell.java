package org.hage.platform.component.simulation.structure;

//// TODO: 17.02.16 add cell identifier (new address)
public interface SimulationCell {
    void performAgentsStep();

    void performControlAgentStep();
}
