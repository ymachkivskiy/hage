package org.hage.platform.component.runtime.migration;

import org.hage.platform.component.runtime.location.AgentsUnitAddress;
import org.hage.platform.simulation.runtime.agent.Agent;

import java.util.List;

public interface OutputMigrationQueue {
    void registerAgentsForMigration(List<Agent> agents, AgentsUnitAddress migrationTarget);
}
