package org.hage.platform.component.runtime;


import org.hage.platform.component.runtime.definition.Population;
import org.hage.platform.component.runtime.event.ActiveUnitsChangedEvent;
import org.hage.platform.component.structure.connections.Position;
import org.hage.platform.util.bus.EventBus;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.ArrayList;
import java.util.List;

import static java.util.Collections.emptyList;

public class LocalExecutionUnitRepository implements ExecutionUnitRepositoryConfigurator {

    @Autowired
    private ExecutionUnitPopulationInitializer unitPopulationInitializer;
    @Autowired
    private EventBus eventBus;

    @Override
    public void populateWith(Population population) {
        // TODO: implement

        List<BaseExecutionUnit> newUnits = new ArrayList<>();

        for (Position position : population.getInternalPositions()) {
            BaseExecutionUnit unit = new BaseExecutionUnit(position);

            unitPopulationInitializer.initializeUnitPopulation(unit, population.unitPopulationFor(position));

            newUnits.add(unit);

        }


        eventBus.post(new ActiveUnitsChangedEvent(newUnits, emptyList()));


    }

}
