package org.hage.platform.component.runtime.unit.api;

import org.hage.platform.component.structure.Position;

public interface AgentsExecutionUnit {
    Position getPosition();

    void performPostProcessing();

    AgentsRunner getAgentsRunner();
}
