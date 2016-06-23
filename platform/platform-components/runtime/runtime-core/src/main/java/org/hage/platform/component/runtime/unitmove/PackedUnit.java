package org.hage.platform.component.runtime.unitmove;

import lombok.Data;
import org.hage.platform.component.structure.Position;
import org.hage.platform.simulation.runtime.agent.Agent;

import java.io.Serializable;
import java.util.List;

@Data
public class PackedUnit implements Serializable {
    private final Position position;
    private final UnitConfiguration configuration;
    private final List<Agent> agents;
}
