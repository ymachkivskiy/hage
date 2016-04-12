package org.hage.platform.component.runtime.unit;

import lombok.extern.slf4j.Slf4j;
import org.hage.platform.HageRuntimeException;
import org.hage.platform.component.runtime.unit.contextadapter.location.UnitLocationContext;
import org.hage.platform.component.runtime.unit.population.UnitPopulationController;
import org.hage.platform.component.structure.Position;
import org.hage.platform.component.structure.connections.Structure;
import org.springframework.beans.factory.annotation.Autowired;

@Slf4j
public  abstract class AgentsUnitFactory {

    @Autowired
    private Structure structure;

    public AgentsUnit create(Position position) {
        log.debug("Initialize agents unit for position {}", position);
        if (!structure.belongsToStructure(position)) {
            // TODO: create own hierarchy of exceptions to runtime module (maybe after it's name change)
            log.error("Illegal position {} given during creation of agents unit.");
            throw new HageRuntimeException("Illegal position " + position + " given during creation of agents unit.");
        }

        return new AgentsUnit(position, createPopulationController(), createUnitLocationContext(position));
    }


    protected abstract UnitPopulationController createPopulationController();

    protected abstract UnitLocationContext createUnitLocationContext(Position position);


}
