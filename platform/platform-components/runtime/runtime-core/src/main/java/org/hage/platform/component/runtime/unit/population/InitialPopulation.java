package org.hage.platform.component.runtime.unit.population;

import lombok.Data;
import org.hage.platform.simulation.runtime.Agent;
import org.hage.platform.simulation.runtime.ControlAgent;

import java.util.List;
import java.util.Optional;

@Data
public class InitialPopulation {
    private final Optional<ControlAgent> controlAgent;
    private final List<Agent> agents;
}
