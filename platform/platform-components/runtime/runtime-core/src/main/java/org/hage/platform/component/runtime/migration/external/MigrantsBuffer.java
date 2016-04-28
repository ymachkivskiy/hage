package org.hage.platform.component.runtime.migration.external;

import org.hage.platform.annotation.di.SingletonComponent;
import org.hage.platform.component.runtime.location.AgentsUnitAddress;
import org.hage.platform.component.runtime.migration.OutputMigrationQueue;
import org.hage.platform.simulation.runtime.agent.Agent;

import java.util.Collection;
import java.util.List;

@SingletonComponent
class MigrantsBuffer implements OutputMigrationQueue, ExternalMigrationGroupsProvider {


    @Override
    public void registerAgentsForMigration(Collection<Agent> agents, AgentsUnitAddress migrationTarget) {
        //todo : NOT IMPLEMENTED

    }

    @Override
    public List<ExternalMigrationGroup> takeExternalMigrationGroups() {
        //todo : NOT IMPLEMENTED
        return null;
    }
}
