package org.hage.platform.component.runtime.unit;

import lombok.extern.slf4j.Slf4j;
import org.hage.platform.component.runtime.execution.change.ActiveExecutionUnitsController;
import org.hage.platform.component.structure.Position;
import org.hage.platform.component.structure.distribution.LocalPositionsController;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

@Slf4j
public class LocalAgentUnitsController {

    @Autowired
    private ActiveExecutionUnitsController activeExecutionUnitsController;
    @Autowired
    private LocalPositionsController localPositionsController;
    @Autowired
    private AgentsUnitFactory unitsFactory;

    private final ConcurrentMap<Position, AgentsUnit> createdUnitsMap = new ConcurrentHashMap<>();

    public AgentsUnit acquireUnitForPosition(Position position) {
        log.debug("Agents unit with for position {} acquired.", position);
        return createdUnitsMap.computeIfAbsent(position, this::createNewUnit);
    }


    private AgentsUnit createNewUnit(Position position) {
        log.debug("Create new agents unit for position {}", position);

        AgentsUnit unit = unitsFactory.create(position);

        activeExecutionUnitsController.activate(unit);
        localPositionsController.activateLocally(position);

        return unit;
    }

    // TODO: write destroy unit method

}
