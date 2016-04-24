package org.hage.platform.component.runtime.unit;

import lombok.extern.slf4j.Slf4j;
import org.hage.platform.annotation.di.SingletonComponent;
import org.hage.platform.component.runtime.populationinit.PopulationLoaderRegistry;
import org.hage.platform.component.runtime.populationinit.UnitPopulationLoader;
import org.hage.platform.component.structure.Position;
import org.hage.platform.component.structure.distribution.LocalPositionsController;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

import static java.util.Collections.emptyList;

@Slf4j
@SingletonComponent
class LocalAgentUnitsController implements PopulationLoaderRegistry {

    @Autowired(required = false)
    private List<AgentUnitActivationAware> unitActivationAwares = emptyList();
    @Autowired(required = false)
    private List<AgentUnitDeactivationAware> unitDeactivationAwares = emptyList();
    @Autowired
    private LocalPositionsController localPositionsController;
    @Autowired
    private AgentsUnitFactory unitsFactory;

    private final ConcurrentMap<Position, AgentsUnit> createdUnitsMap = new ConcurrentHashMap<>();

    public AgentsUnit acquireUnitForPosition(Position position) {
        log.debug("Agents unit with for position {} acquired.", position);
        return createdUnitsMap.computeIfAbsent(position, this::createNewUnit);
    }

    @Override
    public UnitPopulationLoader getPopulationLoaderFor(Position position) {
        return acquireUnitForPosition(position).getUnitPopulationLoader();
    }

    private AgentsUnit createNewUnit(Position position) {
        log.debug("Create new agents unit for position {}", position);

        AgentsUnit unit = unitsFactory.create(position);

        unitActivationAwares.forEach(activationAware -> activationAware.onUnitActivated(unit));
        localPositionsController.activateLocally(position);

        return unit;
    }

    // TODO: write destroy unit method

}
