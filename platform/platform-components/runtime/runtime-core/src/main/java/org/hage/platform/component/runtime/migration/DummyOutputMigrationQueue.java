package org.hage.platform.component.runtime.migration;

import org.hage.platform.annotation.di.SingletonComponent;
import org.hage.platform.component.runtime.location.AgentsUnitAddress;
import org.hage.platform.simulation.runtime.agent.Agent;

import java.util.Collection;

@SingletonComponent
class DummyOutputMigrationQueue implements OutputMigrationQueue {

//    private final Map<>

    @Override
    public void registerAgentsForMigration(Collection<Agent> agents, AgentsUnitAddress migrationTarget) {
        //todo : NOT IMPLEMENTED

    }
}
