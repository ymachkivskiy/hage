package org.hage.platform.component.runtime.unit;

import lombok.extern.slf4j.Slf4j;
import org.hage.platform.HageRuntimeException;
import org.hage.platform.annotation.di.SingletonComponent;
import org.hage.platform.component.runtime.unit.registry.MigrationTargetRegistry;
import org.hage.platform.component.runtime.unit.registry.PopulationLoaderRegistry;
import org.hage.platform.component.runtime.unit.registry.UnitRegistry;
import org.hage.platform.component.structure.Position;
import org.hage.platform.component.structure.connections.Structure;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Collection;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

import static java.util.Collections.unmodifiableCollection;

@Slf4j
@SingletonComponent
class AgentsUnitsController implements PopulationLoaderRegistry, UnitRegistry, MigrationTargetRegistry {

    private final ConcurrentMap<Position, AgentsUnit> createdUnitsConcurrentMap = new ConcurrentHashMap<>();

    @Autowired
    private UnitLifecycleProcessor unitLifecycleProcessor;
    @Autowired
    private Structure structure;

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

    // TODO: write destroy unit method

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
