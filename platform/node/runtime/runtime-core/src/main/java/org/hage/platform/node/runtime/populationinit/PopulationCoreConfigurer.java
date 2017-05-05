package org.hage.platform.node.runtime.populationinit;


import lombok.extern.slf4j.Slf4j;
import org.hage.platform.node.runtime.init.Population;
import org.hage.platform.node.runtime.unit.UnitPopulationLoader;
import org.hage.platform.node.runtime.unit.registry.PopulationLoaderRegistry;
import org.hage.platform.simconf.CoreConfigurerAdapter;
import org.hage.platform.node.structure.Position;
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
