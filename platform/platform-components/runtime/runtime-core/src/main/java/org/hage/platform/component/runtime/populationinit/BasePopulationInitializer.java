package org.hage.platform.component.runtime.populationinit;


import lombok.extern.slf4j.Slf4j;
import org.hage.platform.component.runtime.init.Population;
import org.hage.platform.component.runtime.init.PopulationInitializer;
import org.hage.platform.component.runtime.unit.faces.UnitPopulationLoader;
import org.hage.platform.component.runtime.unit.registry.PopulationLoaderRegistry;
import org.hage.platform.component.structure.Position;
import org.springframework.beans.factory.annotation.Autowired;

@Slf4j
public class BasePopulationInitializer implements PopulationInitializer {
    // TODO: to be rewritten by migration input queue mechanism
    @Autowired
    private PopulationLoaderRegistry localAgentUnitsController;

    @Override
    public void initializeWith(Population population) {
        log.debug("Configure local runtime with population '{}'", population);

        for (Position position : population.getInternalPositions()) {
            log.debug("Initializing unit on {}", position);

            UnitPopulationLoader unitPopulationLoader = localAgentUnitsController.loaderForPosition(position);
            unitPopulationLoader.loadPopulation(population.unitPopulationFor(position));
        }

    }

}
