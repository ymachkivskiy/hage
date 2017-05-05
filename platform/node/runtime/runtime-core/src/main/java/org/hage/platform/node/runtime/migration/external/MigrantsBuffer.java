package org.hage.platform.node.runtime.migration.external;

import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.hage.platform.annotation.di.SingletonComponent;
import org.hage.platform.cluster.api.NodeAddress;
import org.hage.platform.node.runtime.location.AgentsUnitAddress;
import org.hage.platform.node.runtime.migration.OutputMigrationQueue;
import org.hage.platform.node.runtime.migration.internal.InternalMigrationGroup;
import org.hage.platform.simulation.runtime.agent.Agent;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.ArrayList;
import java.util.List;

import static java.util.Collections.singletonList;
import static java.util.stream.Collectors.*;

@SingletonComponent
@Slf4j
class MigrantsBuffer implements OutputMigrationQueue, ExternalMigrationGroupsProvider {

    private static final int INITIAL_CAPACITY = 1024;

    private final ArrayList<Migrants> migrantsList = new ArrayList<>(INITIAL_CAPACITY);

    @Autowired
    private NodeAddressResolver nodeAddressResolver;

    @Override
    public void registerAgentsForMigration(List<Agent> agents, AgentsUnitAddress migrationTarget) {
        Migrants migrants = new Migrants(migrationTarget, agents);

        log.debug("Inserting migrants {}", migrants);

        synchronized (migrantsList) {
            migrantsList.add(migrants);
        }

    }

    @Override
    public List<ExternalMigrationGroup> takeExternalMigrationGroups() {
        log.debug("Taking external migration groups");

        List<Migrants> tmpMigrants;
        synchronized (this.migrantsList) {
            tmpMigrants = new ArrayList<>(this.migrantsList);
            this.migrantsList.clear();
            this.migrantsList.ensureCapacity(INITIAL_CAPACITY);
        }

        List<ExternalMigrationGroup> externalMigrationGroups = createExternalMigrationGroups(tmpMigrants);

        log.debug("External migration groups are {}", externalMigrationGroups);

        return externalMigrationGroups;
    }

    private List<ExternalMigrationGroup> createExternalMigrationGroups(List<Migrants> migrationList) {
        return migrationList
            .stream()
            .map(this::toExternalMigrationGroup)
            .collect(groupingBy(
                ExternalMigrationGroup::getTargetNode,
                mapping(
                    ExternalMigrationGroup::getMigrationGroups,
                    collectingAndThen(toList(), a -> a.stream().flatMap(List::stream).collect(toList()))
                )
            ))
            .entrySet().stream()
            .map(e -> new ExternalMigrationGroup(e.getKey(), e.getValue()))
            .collect(toList());
    }


    private ExternalMigrationGroup toExternalMigrationGroup(Migrants migrants) {
        NodeAddress nodeAddress = nodeAddressResolver.resolveFor(migrants.getTarget());
        return new ExternalMigrationGroup(nodeAddress, singletonList(migrants.toInternalMigrationGroup()));
    }

    @Data
    private static class Migrants {
        private final AgentsUnitAddress target;
        private final List<Agent> agents;

        public InternalMigrationGroup toInternalMigrationGroup() {
            return new InternalMigrationGroup(target.getPosition(), agents);
        }
    }

}
