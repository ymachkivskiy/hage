package org.hage.platform.component.runtime.global;


import lombok.extern.slf4j.Slf4j;
import org.hage.platform.component.runtime.init.Population;
import org.hage.platform.component.runtime.init.PopulationInitializer;
import org.hage.platform.component.runtime.unit.AgentsUnit;
import org.hage.platform.component.runtime.unit.LocalAgentUnitsController;
import org.hage.platform.component.structure.Position;
import org.springframework.beans.factory.annotation.Autowired;

@Slf4j
public class BasePopulationInitializer implements PopulationInitializer {

    @Autowired
    private LocalAgentUnitsController localAgentUnitsController;

    @Override
    public void initializeWith(Population population) {
        log.debug("Configure local runtime with population '{}'", population);

        for (Position position : population.getInternalPositions()) {
            log.debug("Initializing unit on {}", position);

            AgentsUnit unit = localAgentUnitsController.acquireUnitForPosition(position);
            unit.loadPopulation(population.unitPopulationFor(position));
        }

    }

}
