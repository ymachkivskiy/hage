package org.hage.platform.component.runtime.init;


import lombok.extern.slf4j.Slf4j;
import org.hage.platform.component.structure.Position;
import org.springframework.beans.factory.annotation.Autowired;

@Slf4j
public class BaseRuntimeInitializer implements RuntimeInitializer {

    @Autowired
    private UnitInitializationController unitInitializationController;

    @Override
    public void initializeWith(Population population) {
        log.debug("Configure local runtime with population '{}'", population);

        for (Position position : population.getInternalPositions()) {
            log.debug("Initializing unit on {}", position);

            UnitPopulationInitializer initializer = unitInitializationController.getInitializerForUnitOnPosition(position);
            initializer.initializeWith(population.unitPopulationFor(position));
        }

    }

}
