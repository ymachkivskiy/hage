package org.hage.platform.component.runtime.populationinit;


import lombok.extern.slf4j.Slf4j;
import org.hage.platform.component.runtime.init.Population;
import org.hage.platform.component.runtime.unit.faces.UnitPopulationLoader;
import org.hage.platform.component.runtime.unit.registry.PopulationLoaderRegistry;
import org.hage.platform.component.simulationconfig.CoreConfigurerAdapter;
import org.hage.platform.component.structure.Position;
import org.springframework.beans.factory.annotation.Autowired;

@Slf4j
public class PopulationCoreConfigurer extends CoreConfigurerAdapter<Population> {

    @Autowired
    private PopulationLoaderRegistry localAgentUnitsController;

    public PopulationCoreConfigurer(int order) {
        super(config -> config.getSpecific().getPopulation(), order);
    }

    @Override
    protected void configureWithNarrow(Population population) {
        log.debug("Configure local runtime with population '{}'", population);

        for (Position position : population.getInternalPositions()) {
            log.debug("Initializing unit on {}", position);

            UnitPopulationLoader unitPopulationLoader = localAgentUnitsController.loaderForPosition(position);
            unitPopulationLoader.loadPopulation(population.unitPopulationFor(position));
        }

    }

}
