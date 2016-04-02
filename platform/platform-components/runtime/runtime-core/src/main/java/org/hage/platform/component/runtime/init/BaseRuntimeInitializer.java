package org.hage.platform.component.runtime.init;


import org.hage.platform.component.runtime.execution.change.ActiveExecutionUnitsController;
import org.hage.platform.component.runtime.unit.BaseExecutionUnit;
import org.hage.platform.component.structure.Position;
import org.hage.platform.component.structure.distribution.LocalPositionsController;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.ArrayList;
import java.util.List;

public class BaseRuntimeInitializer implements RuntimeInitializer {

    @Autowired
    private ExecutionUnitPopulationInitializer unitPopulationInitializer;
    @Autowired
    private ActiveExecutionUnitsController activeExecutionUnitsController;
    @Autowired
    private LocalPositionsController localPositionsController;

    @Override
    public void initializeWith(Population population) {
        // TODO: implement

        List<BaseExecutionUnit> newUnits = new ArrayList<>();

        for (Position position : population.getInternalPositions()) {
            BaseExecutionUnit unit = new BaseExecutionUnit(position);

            unitPopulationInitializer.initializeUnitPopulation(unit, population.unitPopulationFor(position));

            newUnits.add(unit);

        }

        //// TODO: to be done by some manager
        activeExecutionUnitsController.activate(newUnits);
        localPositionsController.activate(population.getInternalPositions());

    }

}
