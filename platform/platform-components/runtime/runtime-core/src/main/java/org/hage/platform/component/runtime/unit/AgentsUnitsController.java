package org.hage.platform.component.runtime.unit;

import lombok.extern.slf4j.Slf4j;
import org.hage.platform.HageRuntimeException;
import org.hage.platform.annotation.di.SingletonComponent;
import org.hage.platform.component.runtime.migration.internal.InternalMigrantsProvider;
import org.hage.platform.component.runtime.migration.internal.InternalMigrationGroup;
import org.hage.platform.component.runtime.unit.registry.MigrationTargetRegistry;
import org.hage.platform.component.runtime.unit.registry.PopulationLoaderRegistry;
import org.hage.platform.component.runtime.unit.registry.UnitRegistry;
import org.hage.platform.component.runtime.unitmove.PackedUnit;
import org.hage.platform.component.runtime.unitmove.UnitConfiguration;
import org.hage.platform.component.runtime.unitmove.UnitConfigurationActivator;
import org.hage.platform.component.runtime.unitmove.UnitDeactivationPacker;
import org.hage.platform.component.structure.Position;
import org.hage.platform.component.structure.connections.Structure;
import org.hage.platform.simulation.runtime.agent.Agent;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Collection;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

import static java.util.Collections.unmodifiableCollection;
import static java.util.stream.Collectors.toList;

@Slf4j
@SingletonComponent
class AgentsUnitsController implements PopulationLoaderRegistry, UnitRegistry, MigrationTargetRegistry, UnitDeactivationPacker, UnitConfigurationActivator {

    private final ConcurrentMap<Position, AgentsUnit> createdUnitsConcurrentMap = new ConcurrentHashMap<>();

    @Autowired
    private UnitLifecycleProcessor unitLifecycleProcessor;
    @Autowired
    private Structure structure;
    @Autowired
    private InternalMigrantsProvider internalMigrantsProvider;

    @Override
    public AgentsUnit unitFor(Position position) {
        log.debug("Agents unit with for position {} acquired.", position);
        checkPosition(position);

        AgentsUnit unit = createdUnitsConcurrentMap.computeIfAbsent(position, this::createUnit);

        synchronized (unit) {
            unitLifecycleProcessor.performPostConstruction(unit);
        }

        return unit;
    }

    @Override
    public void activateConfigurationOnPosition(Position position, UnitConfiguration unitConfiguration) {
        log.debug("Activate unit configuration on position {}", position);

        AgentsUnit unit = createdUnitsConcurrentMap.merge(position, createUnit(position), (u1, u2) -> {
            log.error("Error while activating unit configuration on position {} - unit already was at node", position);
            throw new HageRuntimeException("Error while activating unit configuration on position " + position);
        });

        synchronized (unit) {
            unitLifecycleProcessor.performPostConstruction(unit, unitConfiguration);
        }

    }

    @Override
    public Collection<? extends Unit> getAllUnits() {
        return unmodifiableCollection(createdUnitsConcurrentMap.values());
    }

    @Override
    public UnitPopulationLoader loaderForPosition(Position position) {
        return unitFor(position);
    }

    @Override
    public AgentMigrationTarget migrationTargetFor(Position position) {
        return unitFor(position);
    }

    @Override
    public PackedUnit deactivateAndPack(Position position) {
        log.debug("Deactivate and pack unit on position {}", position);

        AgentsUnit unit = deactivateUnit(position);

        PackedUnit packedUnit = unit.pack();
        appendMigrantsToPackedUnit(packedUnit);

        return packedUnit;
    }

    private void appendMigrantsToPackedUnit(PackedUnit packedUnit) {
        log.debug("Appending all migrants targeted to {}", packedUnit.getPosition());

        List<InternalMigrationGroup> positionMigrationGroups = internalMigrantsProvider.takeMigrantsTo(packedUnit.getPosition());

        List<Agent> migrants = positionMigrationGroups.stream()
            .map(InternalMigrationGroup::getMigrants)
            .flatMap(List::stream)
            .collect(toList());

        packedUnit.getConfiguration().getAgents().addAll(migrants);
    }

    private AgentsUnit deactivateUnit(Position position) {
        log.debug("Deactivate unit {}", position);

        AgentsUnit unit = createdUnitsConcurrentMap.remove(position);

        if (unit == null) {
            throw new HageRuntimeException("Deactivating unit " + position + " which has been already deactivated or didn't exist");
        }

        unitLifecycleProcessor.performDestruction(unit);

        return unit;
    }

    private void checkPosition(Position position) {
        if (position == null || !structure.belongsToStructure(position)) {
            log.error("Illegal position {} given during creation of agents unit.");
            throw new HageRuntimeException("Illegal position " + position + " given during creation of agents unit.");
        }
    }


    private AgentsUnit createUnit(Position position) {
        log.debug("Create new agent unit for position {}", position);
        return new AgentsUnit(position);
    }
}
