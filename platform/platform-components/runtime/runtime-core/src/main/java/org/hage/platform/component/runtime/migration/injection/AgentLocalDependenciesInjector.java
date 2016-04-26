package org.hage.platform.component.runtime.migration.injection;

import org.hage.platform.simulation.runtime.agent.Agent;

public interface AgentLocalDependenciesInjector {
    void wireDependencies(Agent agent);
}
