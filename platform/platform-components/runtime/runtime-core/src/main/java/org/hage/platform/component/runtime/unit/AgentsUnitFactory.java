package org.hage.platform.component.runtime.unit;

import lombok.extern.slf4j.Slf4j;
import org.hage.platform.HageRuntimeException;
import org.hage.platform.component.runtime.activepopulation.UnitActivePopulationController;
import org.hage.platform.component.runtime.container.UnitAgentCreationController;
import org.hage.platform.component.runtime.location.UnitLocationController;
import org.hage.platform.component.structure.Position;
import org.hage.platform.component.structure.connections.Structure;
import org.springframework.beans.factory.annotation.Autowired;

@Slf4j
public abstract class AgentsUnitFactory {

    @Autowired
    private Structure structure;

    public AgentsUnit create(Position position) {
        checkPosition(position);

        log.debug("Initialize agents unit for position {}", position);

        AgentsUnit unit = new AgentsUnit(position);

        UnitActivePopulationController unitActivePopulationController = createUnitPopulationController(unit);
        UnitLocationController locationContext = createUnitLocationContext(position);
        UnitAgentCreationController unitAgentCreationController = createUnitPopulationContext(unitActivePopulationController);
        AgentContextAdapter agentContextAdapter = new AgentContextAdapter(locationContext, unitAgentCreationController, unitActivePopulationController);

        unit.setUnitLocationController(locationContext);
        unit.setUnitAgentCreationController(unitAgentCreationController);
        unit.setUnitActivePopulationController(unitActivePopulationController);
        unit.setAgentContextAdapter(agentContextAdapter);

        return unit;
    }

    private void checkPosition(Position position) {
        if (!structure.belongsToStructure(position)) {
            log.error("Illegal position {} given during creation of agents unit.");
            throw new HageRuntimeException("Illegal position " + position + " given during creation of agents unit.");
        }
    }

    protected abstract UnitAgentCreationController createUnitPopulationContext(UnitActivePopulationController unitActivePopulationController);

    protected abstract UnitLocationController createUnitLocationContext(Position position);

    protected abstract UnitActivePopulationController createUnitPopulationController(AgentExecutionContextEnvironment agentEnvironment);


}
