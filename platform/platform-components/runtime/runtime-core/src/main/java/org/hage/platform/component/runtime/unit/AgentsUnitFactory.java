package org.hage.platform.component.runtime.unit;

import lombok.extern.slf4j.Slf4j;
import org.hage.platform.HageRuntimeException;
import org.hage.platform.component.runtime.unit.context.AgentLocalEnvironment;
import org.hage.platform.component.runtime.unit.location.UnitLocationContext;
import org.hage.platform.component.runtime.unit.population.UnitAgentCreationContext;
import org.hage.platform.component.runtime.unit.population.UnitPopulationController;
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

        UnitPopulationController unitPopulationController = createUnitPopulationController(unit);
        UnitLocationContext locationContext = createUnitLocationContext(position);
        UnitAgentCreationContext unitAgentCreationContext = createUnitPopulationContext(unitPopulationController);

        unit.setLocationContext(locationContext);
        unit.setUnitAgentCreationContext(unitAgentCreationContext);
        unit.setUnitPopulationController(unitPopulationController);

        unit.setStepCycleAwares(asList(locationContext, unitPopulationController));

        unit.initialize();

        return unit;
    }

    private void checkPosition(Position position) {
        if (!structure.belongsToStructure(position)) {
            log.error("Illegal position {} given during creation of agents unit.");
            throw new HageRuntimeException("Illegal position " + position + " given during creation of agents unit.");
        }
    }

    protected abstract UnitAgentCreationContext createUnitPopulationContext(UnitPopulationController unitPopulationController);

    protected abstract UnitLocationContext createUnitLocationContext(Position position);

    protected abstract UnitPopulationController createUnitPopulationController(AgentLocalEnvironment agentEnvironment);


}
