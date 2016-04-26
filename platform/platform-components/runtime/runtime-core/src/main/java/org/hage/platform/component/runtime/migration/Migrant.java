package org.hage.platform.component.runtime.migration;

import lombok.Data;
import org.hage.platform.component.structure.Position;
import org.hage.platform.simulation.runtime.agent.Agent;

@Data
public class Migrant {
    private final Agent migrantAgent;
    private final Position migrationTarget;
}
