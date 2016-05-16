package org.hage.platform.component.runtime;

import org.hage.platform.component.container.MutableInstanceContainer;
import org.hage.platform.component.runtime.activepopulation.AgentsController;
import org.hage.platform.component.runtime.activepopulation.AgentsTargetEnvironment;
import org.hage.platform.component.runtime.activepopulation.UnitActivePopulationController;
import org.hage.platform.component.runtime.container.AgentsCreator;
import org.hage.platform.component.runtime.container.UnitComponentCreationController;
import org.hage.platform.component.runtime.init.Population;
import org.hage.platform.component.runtime.location.UnitLocationController;
import org.hage.platform.component.runtime.populationinit.GreedyPopulationDivisor;
import org.hage.platform.component.runtime.stateprops.UnitPropertiesController;
import org.hage.platform.component.runtime.stateprops.UnitPropertiesProvider;
import org.hage.platform.component.runtime.unit.UnitLifecycleProcessor;
import org.hage.platform.component.runtime.unit.context.AgentContextAdapter;
import org.hage.platform.component.runtime.unit.context.AgentExecutionContextEnvironment;
import org.hage.platform.component.runtime.util.SimpleStatefulFinisher;
import org.hage.platform.component.runtime.util.StatefulFinisher;
import org.hage.platform.component.structure.Position;
import org.hage.util.proportion.ProportionsDivisor;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

@Configuration
@Import(CoreConfigurersCfg.class)
public class RuntimeCoreCfg {

    @Autowired
    private BeanFactory beanFactory;

    @Bean
    public ProportionsDivisor<Population> populationProportionsDivisor() {
        return new GreedyPopulationDivisor();
    }

    @Bean
    public StatefulFinisher statefulFinisher() {
        return new SimpleStatefulFinisher();
    }

    @Bean
    @Autowired
    public UnitLifecycleProcessor agentsUnitFactory(final MutableInstanceContainer instanceContainer) {

        return new UnitLifecycleProcessor() {

            @Override
            protected UnitComponentCreationController createUnitComponentCreationController(AgentsTargetEnvironment unitActivePopulationController) {
                return beanFactory.getBean(UnitComponentCreationController.class, instanceContainer.newChildContainer(), unitActivePopulationController);
            }

            @Override
            protected UnitLocationController createUnitLocationContext(Position position) {
                return beanFactory.getBean(UnitLocationController.class, position);
            }

            @Override
            protected UnitActivePopulationController createUnitPopulationController(AgentExecutionContextEnvironment agentEnvironment) {
                return beanFactory.getBean(UnitActivePopulationController.class, agentEnvironment);
            }

            @Override
            protected AgentContextAdapter createAgentContextAdapter(UnitLocationController locationController, AgentsCreator agentsCreator, AgentsController agentsController, UnitPropertiesProvider localUnitPropertiesProvider) {
                return beanFactory.getBean(AgentContextAdapter.class, locationController, agentsCreator, agentsController, localUnitPropertiesProvider);
            }

            @Override
            protected UnitPropertiesController createUnitPropertiesController(Position position, UnitComponentCreationController componentCreationController) {
                return beanFactory.getBean(UnitPropertiesController.class, position, componentCreationController);
            }
        };

    }

}
