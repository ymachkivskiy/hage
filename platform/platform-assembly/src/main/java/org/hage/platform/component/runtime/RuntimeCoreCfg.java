package org.hage.platform.component.runtime;

import org.hage.platform.component.container.MutableInstanceContainer;
import org.hage.platform.component.runtime.activepopulation.AgentsController;
import org.hage.platform.component.runtime.activepopulation.UnitActivePopulationController;
import org.hage.platform.component.runtime.container.AgentsCreator;
import org.hage.platform.component.runtime.container.UnitAgentCreationController;
import org.hage.platform.component.runtime.init.Population;
import org.hage.platform.component.runtime.init.PopulationInitializer;
import org.hage.platform.component.runtime.location.UnitLocationController;
import org.hage.platform.component.runtime.populationinit.BasePopulationInitializer;
import org.hage.platform.component.runtime.populationinit.GreedyPopulationDivisor;
import org.hage.platform.component.runtime.unit.AgentContextAdapter;
import org.hage.platform.component.runtime.unit.AgentExecutionContextEnvironment;
import org.hage.platform.component.runtime.unit.AgentsUnitFactory;
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
@Import(MigrationCfg.class)
public class RuntimeCoreCfg {

    @Autowired
    private BeanFactory beanFactory;

    @Bean
    public ProportionsDivisor<Population> populationProportionsDivisor() {
        return new GreedyPopulationDivisor();
    }

    @Bean
    public PopulationInitializer runtimeInitializer() {
        return new BasePopulationInitializer();
    }

    @Bean
    public StatefulFinisher statefulFinisher() {
        return new SimpleStatefulFinisher();
    }

    @Bean
    @Autowired
    public AgentsUnitFactory agentsUnitFactory(final MutableInstanceContainer instanceContainer) {

        return new AgentsUnitFactory() {

            @Override
            protected UnitAgentCreationController createUnitPopulationContext(UnitActivePopulationController unitActivePopulationController) {
                return beanFactory.getBean(UnitAgentCreationController.class, instanceContainer.newChildContainer(), unitActivePopulationController);
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
            protected AgentContextAdapter createAgentContextAdapter(UnitLocationController locationController, AgentsCreator agentsCreator, AgentsController agentsController) {
                return beanFactory.getBean(AgentContextAdapter.class, locationController, agentsCreator, agentsController);
            }
        };

    }

}
