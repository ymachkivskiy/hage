package org.hage.platform.component.runtime.unit;

import lombok.extern.slf4j.Slf4j;
import org.hage.platform.component.runtime.execution.change.ActiveExecutionUnitsController;
import org.hage.platform.component.runtime.init.UnitInitializationController;
import org.hage.platform.component.runtime.init.UnitPopulationInitializer;
import org.hage.platform.component.structure.Position;
import org.hage.platform.component.structure.distribution.LocalPositionsController;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.HashMap;
import java.util.Map;

@Slf4j
public class NodeAgentUnitsRepo implements UnitInitializationController {


    @Autowired
    private ActiveExecutionUnitsController activeExecutionUnitsController;
    @Autowired
    private LocalPositionsController localPositionsController;
    @Autowired
    private AgentsUnitFactory unitsFactory;

    private final Map<Position, AgentsUnit> createdUnitsMap = new HashMap<>();

    private AgentsUnit getOrCreateLocalUnit(Position position) {

        AgentsUnit unit = unitsFactory.create(position);

        createdUnitsMap.put(position, unit);

        activeExecutionUnitsController.activate(unit);
        localPositionsController.activate(position);

        return unit;
    }



    @Override
    public UnitPopulationInitializer getInitializerForUnitOnPosition(Position position) {
        return getOrCreateLocalUnit(position);
    }

}
