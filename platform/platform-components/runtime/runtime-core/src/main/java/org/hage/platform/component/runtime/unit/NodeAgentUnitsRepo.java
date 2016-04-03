package org.hage.platform.component.runtime.unit;

import lombok.extern.slf4j.Slf4j;
import org.hage.platform.HageRuntimeException;
import org.hage.platform.component.container.MutableInstanceContainer;
import org.hage.platform.component.runtime.execution.change.ActiveExecutionUnitsController;
import org.hage.platform.component.runtime.init.UnitInitializationController;
import org.hage.platform.component.runtime.init.UnitPopulationInitializer;
import org.hage.platform.component.runtime.unit.population.UnitPopulationController;
import org.hage.platform.component.structure.Position;
import org.hage.platform.component.structure.connections.Structure;
import org.hage.platform.component.structure.distribution.LocalPositionsController;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.HashMap;
import java.util.Map;

@Slf4j
public class NodeAgentUnitsRepo implements UnitInitializationController {

    @Autowired
    private BeanFactory beanFactory;
    @Autowired
    private ActiveExecutionUnitsController activeExecutionUnitsController;
    @Autowired
    private LocalPositionsController localPositionsController;
    @Autowired
    private MutableInstanceContainer mutableInstanceContainer;
    @Autowired
    private Structure structure;

    private final Map<Position, AgentsUnit> createdUnitsMap = new HashMap<>();

    private AgentsUnit getOrCreateLocalUnit(Position position) {
        log.debug("Initialize agents unit for position {}", position);
        if (!structure.belongsToStructure(position)) {
            // TODO: create own hierarchy of exceptions to runtime module (maybe after it's name change)
            log.error("Illegal position {} given during creation of agents unit.");
            throw new HageRuntimeException("Illegal position " + position + " given during creation of agents unit.");
        }

        // TODO: map managing
        AgentsUnit unit = new AgentsUnit(position, newPopulationController());

        createdUnitsMap.put(position, unit);

        activeExecutionUnitsController.activate(unit);
        localPositionsController.activate(position);

        return unit;
    }

    private UnitPopulationController newPopulationController() {
        return beanFactory.getBean(UnitPopulationController.class, mutableInstanceContainer.newChildContainer());
    }

    @Override
    public UnitPopulationInitializer getInitializerForUnitOnPosition(Position position) {
        return getOrCreateLocalUnit(position);
    }

}
