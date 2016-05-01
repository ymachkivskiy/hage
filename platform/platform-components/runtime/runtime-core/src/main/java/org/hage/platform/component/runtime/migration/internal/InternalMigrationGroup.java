package org.hage.platform.component.runtime.migration.internal;

import lombok.Data;
import org.hage.platform.component.structure.Position;
import org.hage.platform.simulation.runtime.agent.Agent;

import java.io.Serializable;
import java.util.List;

@Data
public class InternalMigrationGroup implements Serializable {
    private final Position targetPosition;
    private final List<Agent> migrants;
}
