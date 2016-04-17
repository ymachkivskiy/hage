package org.hage.platform.component.runtime.unit;

import lombok.extern.slf4j.Slf4j;
import org.hage.platform.HageRuntimeException;
import org.hage.platform.component.runtime.unit.location.UnitLocationContext;
import org.hage.platform.component.runtime.unit.population.UnitPopulationModificationContext;
import org.hage.platform.component.runtime.unit.population.PopulationController;
import org.hage.platform.component.structure.Position;
import org.hage.platform.component.structure.connections.Structure;
import org.springframework.beans.factory.annotation.Autowired;

import static java.util.Arrays.asList;

@Slf4j
public abstract class AgentsUnitFactory {

    @Autowired
    private Structure structure;

    public AgentsUnit create(Position position) {
        checkPosition(position);

        log.debug("Initialize agents unit for position {}", position);

        AgentsUnit unit = new AgentsUnit();

        PopulationController populationController = new PopulationController(unit);
        UnitLocationContext locationContext = createUnitLocationContext(position);
        UnitPopulationModificationContext unitPopulationModificationContext = createUnitPopulationContext(populationController);

        unit.setLocationContext(locationContext);
        unit.setUnitPopulationModificationContext(unitPopulationModificationContext);
        unit.setPopulationController(populationController);

        unit.setStepCycleAwares(asList(locationContext, populationController));

        unit.initialize();

        return unit;
    }

    private void checkPosition(Position position) {
        if (!structure.belongsToStructure(position)) {
            // TODO: create own hierarchy of exceptions to runtime module (maybe after it's name change)
            log.error("Illegal position {} given during creation of agents unit.");
            throw new HageRuntimeException("Illegal position " + position + " given during creation of agents unit.");
        }
    }

    protected abstract UnitPopulationModificationContext createUnitPopulationContext(PopulationController populationController);

    protected abstract UnitLocationContext createUnitLocationContext(Position position);


}
